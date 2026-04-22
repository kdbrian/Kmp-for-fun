package com.farmconnect.client.ui.state

import android.Manifest
import android.R.attr.description
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmconnect.client.ui.index.cart.CartScreenUiState
import com.farmconnect.client.ui.index.checkout.payment.PaymentMethodUiState
import com.farmconnect.core.domain.dto.OrderDetailsDto
import com.farmconnect.core.domain.dto.PaymentUtils
import com.farmconnect.core.domain.dto.ProductOrServiceItemDto
import com.farmconnect.core.domain.model.CartItem
import com.farmconnect.core.domain.model.ContactDetailsDomain
import com.farmconnect.core.domain.model.toDomain
import com.farmconnect.core.domain.service.NotificationService
import com.farmconnect.core.domain.service.OrderService
import com.farmconnect.core.domain.service.PaymentService
import com.farmconnect.core.domain.usecases.DeleteContactInfoByIdUseCase
import com.farmconnect.core.domain.usecases.RetrieveContactDetailsUseCase
import com.farmconnect.core.domain.usecases.SaveContactInfoUseCase
import com.farmconnect.core.ui.R
import com.farmconnect.core.ui.util.showToast
import com.farmconnect.core.ui.util.toCurrency
import com.farmconnect.core.util.LocationUtils.getLocationName
import com.farmconnect.core.util.NotificationUtils
import com.farmconnect.core.util.NotificationUtils.showNotification
import com.farmconnect.core.util.PaymentMethods
import com.farmconnect.core.util.Resource
import com.farmconnect.core.util.await
import com.farmconnect.core.util.resolveLocationName
import com.farmconnect.core.util.resolveLocationNameOrCoordinates
import com.farmconnect.core.util.toLatLng
import com.farmconnect.datastore.AppDatastore
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.serialization.json.Json
import Napie.log.Napie
import kotlin.math.min
import kotlin.time.Duration.Companion.seconds

private val Context.cartCache by preferencesDataStore(
    "cart"
)

class CartScreenViewModel(
    private val context: Context,
    private val fusedLocationClientProvider: FusedLocationProviderClient,
    private val orderService: OrderService,
    private val paymentService: PaymentService,
    private val appDatastore: AppDatastore,
    private val notificationService: NotificationService,
    private val saveContactInfoUseCase: SaveContactInfoUseCase,
    private val deleteContactInfoByIdUseCase: DeleteContactInfoByIdUseCase,
    private val retrieveContactDetailsUseCase: RetrieveContactDetailsUseCase
) : ViewModel(), DefaultLifecycleObserver {

    private val cartKey = stringPreferencesKey("cart")
    private val json = Json { ignoreUnknownKeys = true }

    private val _cartScreenUiState = MutableStateFlow(CartScreenUiState(cartItems = null))
    val cartScreenUiState = _cartScreenUiState.asStateFlow()

    private val _paymentMethodUiState = MutableStateFlow(PaymentMethodUiState())
    val paymentMethodUiState = _paymentMethodUiState.asStateFlow()

    private val _cartNotificationsChannel = Channel<Resource<String>>()
    val cartNotificationsChannel = _cartNotificationsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            restoreCart()
            prefillUserInfo()
            loadContacts()
        }

        // Observe cart changes to sync totals + payment amount.
        // Uses a derived flow so we never write back into the same StateFlow
        // we are reading from — avoids the recursive-update loop.
        viewModelScope.launch {
            _cartScreenUiState
                .map { it.cartItems }           // only re-run when items actually change
                .distinctUntilChanged()
                .collect { items ->
                    val beforeDisc = items?.sumOf {
                        (it.product.price ?: 0.0) * it.count
                    } ?: 0.0

                    val afterDisc = recalculateTotal(items)

                    Napie.d("before: $beforeDisc, after: $afterDisc")

                    // Single, targeted update — does NOT re-trigger this collector
                    // because we are watching `cartItems`, not `cartTotal`/`discountTotal`
                    _cartScreenUiState.update { state ->
                        state.copy(
                            cartTotal = afterDisc,
                            discountTotal = beforeDisc - afterDisc
                        )
                    }

                    _paymentMethodUiState.update { it.copy(amount = afterDisc) }
                }
        }

        // Persist cart with debounce to avoid hammering DataStore on every keystroke
        viewModelScope.launch {
            _cartScreenUiState
                .debounce(300)
                .collect { saveCart(it) }
        }
    }

    private suspend fun restoreCart() {
        context.cartCache.data
            .map { prefs ->
                prefs[cartKey]
                    ?.takeIf { it.isNotEmpty() }
                    ?.let { runCatching { json.decodeFromString<CartScreenUiState>(it) }.getOrNull() }
                    ?: emptyCartState()
            }
            .filter { it.cartItems?.isNotEmpty() == true }
            .debounce(200)
            .flowOn(Dispatchers.IO)           // decode on IO; collect on main
            .first()                          // take only the first valid snapshot
            .let { restored ->
                Napie.d("Cache restored: ${restored.cartItems?.size}")
                _cartScreenUiState.value = restored
            }
    }

    private suspend fun prefillUserInfo() {
        appDatastore.appUser()?.phone?.let { onNumberUpdate(it) }
    }

    private suspend fun loadContacts() {
        _cartScreenUiState.update {
            it.copy(contacts = retrieveContactDetailsUseCase.invoke().first())
        }
    }

    private fun emptyCartState() = CartScreenUiState(
        cartItems = null,
        locationName = null,
        location = emptyList(),
        phoneNumber = null,
        useMyLocation = false,
        contactDetails = null,
        contacts = null,
        cartTotal = 0.0,
    )

    fun saveCart(state: CartScreenUiState) {
        viewModelScope.launch(Dispatchers.IO) {
            context.cartCache.edit { prefs ->
                prefs[cartKey] = json.encodeToString(state)
            }
        }
    }

    private fun computeFinalPrice(p: ProductOrServiceItemDto): Double {
        val base = p.price ?: 0.0
        return if (p.isDiscounted == true && p.discount != null) {
            val percent = p.discount!!.coerceIn(0.0, 100.0)
            base * (1 - percent / 100.0)
        } else base
    }

    private fun recalculateTotal(cart: List<CartItem>?): Double =
        cart?.sumOf { computeFinalPrice(it.product) * it.count } ?: 0.0

    fun addProductToCart(product: ProductOrServiceItemDto) {
        val current = _cartScreenUiState.value.cartItems.orEmpty()
        val existing = current.find { it.product.id == product.id }

        val updatedList = if (existing != null) {
            val maxStock = product.stock?.toInt() ?: Int.MAX_VALUE
            current.map {
                if (it.product.id == product.id)
                    it.copy(count = (it.count + 1).coerceAtMost(maxStock))
                else it
            }
        } else {
            current + CartItem(product, count = 1, description = "")
        }

        Napie.tag("addProductToCart").d("cart[${current.size} → ${updatedList.size}]")

        _cartScreenUiState.update {
            it.copy(
                cartItems = updatedList,
                cartTotal = recalculateTotal(updatedList)
            )
        }
    }

    fun removeProduct(productId: String) {
        val updatedList = _cartScreenUiState.value.cartItems
            .orEmpty()
            .filterNot { it.product.id == productId }

        _cartScreenUiState.update {
            it.copy(
                cartItems = updatedList.ifEmpty { null },   // null = empty cart, drives empty-state UI
                cartTotal = recalculateTotal(updatedList)
            )
        }
    }

    fun updateProductCount(product: ProductOrServiceItemDto, updatedCount: Int) {
        if (updatedCount == 0) {
            removeProduct(product.id.toString())
            return
        }

        val maxStock = product.stock?.toInt() ?: Int.MAX_VALUE
        val safeCount = updatedCount.coerceIn(1, maxStock)

        val updatedList = _cartScreenUiState.value.cartItems.orEmpty().map {
            if (it.product.id == product.id) it.copy(count = safeCount) else it
        }

        _cartScreenUiState.update {
            it.copy(
                cartItems = updatedList,
                cartTotal = recalculateTotal(updatedList)
            )
        }
    }

    fun updateCartItemDescription(productId: String, description: String) {
        if (description.length >= 200) return

        val items = _cartScreenUiState.value.cartItems ?: return
        val index = items.indexOfFirst { it.product.id == productId }
        if (index == -1) return

        val updatedList = items.toMutableList().also {
            it[index] = it[index].copy(description = description)
        }

        _cartScreenUiState.update { it.copy(cartItems = updatedList) }
    }

    fun clearCart() {
        viewModelScope.launch {
            _cartScreenUiState.update { emptyCartState() }
            _paymentMethodUiState.update { it.copy(resource = Resource.Idle()) }
            context.cartCache.edit { it.remove(cartKey) }
        }
    }

    fun onNumberUpdate(number: String) {
        _cartScreenUiState.update { it.copy(phoneNumber = number) }
        _paymentMethodUiState.update { it.copy(paymentPhoneNumber = number) }
    }

    fun onLocationSelected(latLng: LatLng) {
        viewModelScope.launch {
            _cartScreenUiState.update {
                it.copy(
                    location = listOf(latLng.latitude, latLng.longitude),
                    locationName = latLng.resolveLocationName(context)
                )
            }
        }
    }

    fun onPlaceSelected(place: Place) {
        _cartScreenUiState.update { uiState ->
            val loc = place.location ?: return@update uiState.copy(locationName = place.displayName)
            uiState.copy(
                locationName = place.displayName
                    ?: context.getLocationName(loc.latitude, loc.longitude),
                location = listOf(loc.latitude, loc.longitude)
            )
        }
    }

    fun deleteContact(id: String) {
        viewModelScope.launch { deleteContactInfoByIdUseCase.invoke(id) }
    }

    fun onPaymentMethodSelected(paymentMethod: PaymentMethods) {
        _paymentMethodUiState.update { it.copy(selectedPaymentMethod = paymentMethod) }
    }

    fun onCardNumberUpdate(number: String) {
        _paymentMethodUiState.update { it.copy(cardNumber = number) }
    }

    fun onCvCUpdate(cvv: String) {
        _paymentMethodUiState.update { it.copy(cvcNumber = cvv) }
    }

    fun onCardExpiryUpdate(expiry: String) {
        _paymentMethodUiState.update { it.copy(cardExpiryDate = expiry) }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun startLocationListener(listenMyLocation: Boolean = false) {
        _cartScreenUiState.update { it.copy(useMyLocation = listenMyLocation) }

        if (!listenMyLocation) return

        viewModelScope.launch {
            _cartNotificationsChannel.trySend(Resource.Success("Listening live location."))
            fusedLocationClientProvider.lastLocation.asDeferred().await()?.let { location ->
                _cartScreenUiState.update {
                    it.copy(
                        location = listOf(location.latitude, location.longitude),
                        locationName = listOf(location.latitude, location.longitude)
                            .toLatLng()
                            .resolveLocationNameOrCoordinates(context)
                    )
                }
            }
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun placeOrder() {
        val uiState = _cartScreenUiState.value
        if (uiState.cartItems.isNullOrEmpty()) return

        viewModelScope.launch {
            _paymentMethodUiState.update { it.copy(resource = Resource.Loading()) }

            val contactDetails = ContactDetailsDomain(
                location = uiState.location,
                locationName = uiState.locationName,
                email = appDatastore.appUser()?.email,
                phoneNumber = uiState.phoneNumber ?: appDatastore.appUser()?.phone
            )

            val orderDetailsDto = OrderDetailsDto(
                products = uiState.cartItems.map { (product, count) ->
                    mapOf(
                        "productId" to product.toString(),
                        "productTitle" to product.title.toString(),
                        "productTotal" to (computeFinalPrice(product) * count).toCurrency(),
                        "business" to product.metadata["businessId"].toString(),
                        "productDiscount" to product.discount.toString(),
                        "productImageUrl" to product.images.randomOrNull().toString(),
                        "count" to count.toString(),
                        "description" to description.toString(),
                        "status" to "pending"
                    )
                },
                contactDetails = contactDetails,
                totalDiscount = uiState.discountTotal,
                productsTotal = uiState.cartTotal,
                total = uiState.cartTotal,
            )

            _cartNotificationsChannel.trySend(Resource.Success("Placing order."))

            orderService.placeOrder(orderDto = orderDetailsDto)
                .onSuccess { orderId ->
                    handleOrderSuccess(orderId, contactDetails, orderDetailsDto)
                }
                .onFailure { err ->
                    Napie.d("Order failed: ${err.message}")
                    _cartNotificationsChannel.trySend(Resource.Error("Failed to place order: ${err.message}"))
                    _paymentMethodUiState.update {
                        it.copy(resource = Resource.Error(err.message ?: "Something went wrong"))
                    }
                    delay(3.seconds)
                    _paymentMethodUiState.update { it.copy(resource = Resource.Idle()) }
                }
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private suspend fun handleOrderSuccess(
        orderId: String,
        contactDetails: ContactDetailsDomain,
        orderDetailsDto: OrderDetailsDto
    ) {
        Napie.d("Order placed: $orderId")
        saveContactInfoUseCase.invoke(contactDetails)
        context.showToast("Order placed successfully")

        val notificationData = NotificationUtils.NotificationData.Builder().apply {
            val shortId = orderId.substring(1, min(6, orderId.length))
            val message =
                "Your order$shortId has been placed successfully. Visit order history to view details."
            setChannelId("1")
            setTitle("Order placed")
            setMessage(message)
            setBigText(message)
            setSmallIcon(R.drawable.order_placed)
            setAutoCancel(true)
            setPriority(NotificationCompat.PRIORITY_HIGH)
        }.build()

        notificationService.saveNotification(notificationData.toDomain())

        if (appDatastore.isNotificationAllowed())
            context.showNotification(data = notificationData, notificationId = 1)

        context.cartCache.edit { it.remove(cartKey) }

        paymentService.savePaymentRequest(
            payment = PaymentUtils.MPesaPaymentRecord.Builder().apply {
                contactDetails.phoneNumber?.let { phone ->
                    when {
                        phone.startsWith("01") -> phoneNumber(phone.replaceFirst("01", "2541"))
                        phone.startsWith("07") -> phoneNumber(phone.replaceFirst("07", "2547"))
                    }
                }
                subject(appDatastore.uid())
                amount(orderDetailsDto.total)
                commodity(orderId)
                method(_paymentMethodUiState.value.selectedPaymentMethod)
            }
        ).onSuccess { ref ->
            _cartNotificationsChannel.trySend(Resource.Success("Processing payment."))
            requestMpesaPayment(ref, orderId)
        }.onFailure { err ->
            Napie.d("Payment save failed: ${err.message}")
            _cartNotificationsChannel.trySend(Resource.Error("Failed to place order: ${err.message}"))
            _paymentMethodUiState.update {
                it.copy(resource = Resource.Error(err.message ?: "Something went wrong"))
            }
        }
    }

    fun requestMpesaPayment(ref: String, orderId: String) {
        viewModelScope.launch {
            paymentService.requestsMpesaPayment(ref)
                .onSuccess { res ->
                    if (res.success) {
                        val paymentRecord =
                            paymentService.loadRequestsByCommodityId(orderId).await()
                        Napie.d("paymentRecord: $paymentRecord")

                        _paymentMethodUiState.update {
                            it.copy(
                                resource = Resource.Success(
                                    paymentRecord?.stkPush?.get("CustomerMessage").toString()
                                ),
                                paymentInfo = paymentRecord
                            )
                        }

                        _cartNotificationsChannel.send(Resource.Idle())
                        _cartScreenUiState.update {
                            CartScreenUiState(orderId = orderId, cartItems = null)
                        }
                    } else {
                        _paymentMethodUiState.update {
                            it.copy(resource = Resource.Error(res.message))
                        }
                    }
                }
                .onFailure {
                    Napie.d("M-Pesa request failed: ${it.message}")
                    _paymentMethodUiState.update { state ->
                        state.copy(
                            resource = Resource.Error(it.message?.drop(8) ?: "Something went wrong")
                        )
                    }
                }
        }
    }

    fun retryPayment(orderId: String) {
        if (_paymentMethodUiState.value.selectedPaymentMethod != PaymentMethods.Mpesa) return

        viewModelScope.launch {
            _paymentMethodUiState.update { it.copy(resource = Resource.Loading()) }

            paymentService.loadRequestsByCommodityId(orderId)
                .onSuccess { payment ->
                    payment?.id?.let { id ->
                        paymentService.requestsMpesaPayment(id)
                            .onSuccess {
                                _cartNotificationsChannel.send(Resource.Success("Request submitted."))
                                _paymentMethodUiState.update {
                                    it.copy(resource = Resource.Success("Request submitted."))
                                }
                            }
                            .onFailure {
                                _cartNotificationsChannel.send(Resource.Error("Something unexpected happened."))
                                _paymentMethodUiState.update {
                                    it.copy(resource = Resource.Error("Something unexpected happened."))
                                }
                            }
                    }
                }
                .onFailure {
                    _cartNotificationsChannel.send(Resource.Error("Something unexpected happened."))
                    _paymentMethodUiState.update {
                        it.copy(resource = Resource.Error("Something unexpected happened."))
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Napie.d("onCleared")
        saveCart(_cartScreenUiState.value)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        owner.lifecycle.removeObserver(this)
        saveCart(_cartScreenUiState.value)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        saveCart(_cartScreenUiState.value)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        saveCart(_cartScreenUiState.value)
    }
}