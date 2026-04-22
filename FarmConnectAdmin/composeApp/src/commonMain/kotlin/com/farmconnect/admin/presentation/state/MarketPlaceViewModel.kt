package com.farmconnect.admin.presentation.state

import androidx.core.bundle.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.flatMap
import androidx.paging.map
import com.farmconnect.admin.core.domain.dto.CategoryItemDto
import com.farmconnect.admin.core.domain.dto.ProductTemplateDto
import com.farmconnect.admin.core.domain.model.BugReport
import com.farmconnect.admin.core.domain.service.AppUserService
import com.farmconnect.admin.core.domain.service.AuthenticationService
import com.farmconnect.admin.core.domain.service.CategoriesService
import com.farmconnect.admin.core.domain.service.IssueService
import com.farmconnect.admin.core.domain.service.NotificationService
import com.farmconnect.admin.core.domain.service.OrderService
import com.farmconnect.admin.core.domain.service.PaymentService
import com.farmconnect.admin.core.domain.service.ProductService
import com.farmconnect.admin.core.domain.service.ProductTemplateService
import com.farmconnect.admin.presentation.state.MarketPlaceEvent
import com.farmconnect.core.ui.util.DateUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.flatMap

data class DetailsUiState(
    val categoryItemDto: CategoryItemDto? = null,
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

class MarketPlaceViewModel(
    private val authenticationService: AuthenticationService,
    private val categoriesService: CategoriesService,
    private val productService: ProductService,
    private val paymentService: PaymentService,
    private val issueService: IssueService,
    private val orderService: OrderService,
    private val remoteProductTemplateService: ProductTemplateService,
    private val localProductTemplateService: ProductTemplateService,
    private val appDatastore: AppDatastore,
    private val appUserService: AppUserService,
    private val notificationsService: NotificationService,
) : ViewModel() {

    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    private val _uiState = MutableStateFlow(MarketPlaceUiState())
    val uiState = _uiState.asStateFlow()

    private val templates = MutableStateFlow<List<ProductTemplateDto>>(emptyList())

    private val _searchUiState = MutableStateFlow(SearchUiState())
    val searchUiState = _searchUiState.asStateFlow()

    private val _productDetailsUiState =
        MutableStateFlow(ProductDetailsUiState(similarProducts = emptyList()))
    val productDetailsUiState = _productDetailsUiState.asStateFlow()

    private val _notificationsDetailsUiState = MutableStateFlow(NotificationsUiState())
    val notificationsDetailsUiState = _notificationsDetailsUiState.asStateFlow()

    private val _detailsUiState = MutableStateFlow(DetailsUiState())
    val detailsUiState = _detailsUiState.asStateFlow()

    private val _orderScreenUiState =
        MutableStateFlow(OrderScreenUiState(orderDetails = emptyList()))

    private val _paymentMethodsUiState = MutableStateFlow(PaymentMethodUiState())
    val paymentMethodsUiState = _paymentMethodsUiState.asStateFlow()

    val orderScreenUiState = _orderScreenUiState.asStateFlow()

    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState = _profileUiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    val products = combine(
        _searchQuery,
        _selectedCategory
    ) { query, category ->
        query to category
    }.flatMapLatest { (query, category) ->
        productService
            .listProductsPaged()
            .filterNotNull()
            .map { pagingData ->
                pagingData.filter { product ->
                    when {
                        query.isNotBlank() ->
                            product.title?.contains(query, true) == true ||
                                    product.description?.contains(query, true) == true

                        category != null ->
                            product.categories.contains(category)

                        else -> true
                    }
                }
            }
    }.cachedIn(viewModelScope)

    val categories = products
        .distinctUntilChanged()
        .debounce(300)
        .map {
            it.flatMap {
                it
                    .categories
                    .distinct()
                    .map { cat ->
                        CategoryItemDto(
                            categoryName = cat
                        )
                    }

            }
        }
        .cachedIn(viewModelScope)

    init {
        reloadProfile()
        loadCategories()
        loadOrders()
        loadTemplates()
        loadNotifications()

        viewModelScope.launch {
            _notificationsDetailsUiState.update {
                it.copy(isNotificationAllowed = appDatastore.isNotificationAllowed())
            }

            _orderScreenUiState
                .map { it.selectedOrder }
                .distinctUntilChangedBy { it?.orderId }
                .onEach { orderDetailsDto ->
                    orderDetailsDto?.orderId?.let { orderId ->
                        paymentService
                            .loadRequestsByCommodityId(orderId)
                            .onSuccess { paymentInfo ->
                                _orderScreenUiState.update {
                                    it.copy(paymentInfo = paymentInfo)
                                }
                            }
                    }
                }
                .launchIn(this)
        }
    }

    fun reloadProfile() {
        viewModelScope.launch {
            appDatastore.appUser()?.let { appUser ->
                appUserService.profileById(appUser.uid).onSuccess { user ->
                    user?.let {
                        _profileUiState.update {
                            it.copy(
                                isEmailVerified = appUserService.isEmailVerified,
                                appUser = user,
                                mode = appDatastore.mode(),
                                isNotificationAllowed = appDatastore.isNotificationAllowed()
                            )
                        }
                    }
                }
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            categoriesService.loadCategories().onSuccess { categoryItemDtos ->
                val dtos = Defaults.CategoryItems.categoryItems.map { it.toDto() }
                    .plus(categoryItemDtos)
                    .distinctBy { it.categoryName }

                _searchUiState.update {
                    it.copy(categories = dtos)
                }
            }
        }
    }

    fun loadTemplates() {
        remoteProductTemplateService.loadTemplates()
            .onEach { result ->
                result.onSuccess { templateDtos ->
                    templates.update { it + templateDtos }
                    updateCategoriesFromTemplates(templateDtos)
                }
            }
            .launchIn(viewModelScope)

        localProductTemplateService.loadTemplates()
            .onEach { result ->
                result.onSuccess { templateDtos ->
                    templates.update { it + templateDtos }
                    updateCategoriesFromTemplates(templateDtos)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun updateCategoriesFromTemplates(templateDtos: List<ProductTemplateDto>) {
        _searchUiState.update { currentState ->
            val newCategories = templateDtos.flatMap { it.categories }
                .map { CategoryItemDto(categoryName = it) }

            currentState.copy(
                categories = (currentState.categories + newCategories)
                    .distinctBy { it.categoryName }
            )
        }
    }

    fun loadNotifications() {
        viewModelScope.launch {
            notificationsService.loadNotifications()
                .distinctUntilChanged()
                .onEach { notifications ->
                    _notificationsDetailsUiState.update {
                        it.copy(notifications = notifications)
                    }
                }
                .launchIn(this)
        }
    }

    fun loadOrders() {
        viewModelScope.launch {
            val uid = appDatastore.uid()
            orderService.loadOrdersByKey("userId", uid)
                .distinctUntilChanged()
                .onEach { result ->
                    result.onSuccess { orderDetailsDtos ->
                        _orderScreenUiState.update {
                            it.copy(
                                orderDetails = orderDetailsDtos.sortedByDescending { it.placeOn }
                            )
                        }
                    }
                }
                .launchIn(this)
        }
    }

    fun updateIssue(issue: String) {
        _profileUiState.update { it.copy(issue = issue) }
    }

    fun reportIssue() {
        viewModelScope.launch {
            val issue = _profileUiState.value.issue ?: return@launch
            issueService.reportIssue(
                BugReport.Builder()
                    .issueText(issue)
                    .addTag("user-reported")
                    .addTag(
                        "day-${
                            System.currentTimeMillis()
                                .toFormattedDate(DateUtils.FORMAT_FULL)
                        }"
                    )
                    .build()
            ).onSuccess {
                _profileUiState.update { it.copy(issue = null) }
            }
        }
    }

    fun deleteNotification(notificationId: String) {
        viewModelScope.launch {
            notificationsService.deleteNotification(notificationId)
        }
    }

    fun toggleNotification() {
        viewModelScope.launch {
            appDatastore.setNotificationsAllowed(!appDatastore.isNotificationAllowed())
            _notificationsDetailsUiState.update {
                it.copy(isNotificationAllowed = appDatastore.isNotificationAllowed())
            }
        }
    }

    fun onEvent(event: MarketPlaceEvent) {
        viewModelScope.launch {
            when (event) {
                is MarketPlaceEvent.ViewProduct -> {
                    firebaseAnalytics.logEvent(
                        FirebaseAnalytics.Event.VIEW_ITEM,
                        bundleOf(
                            FirebaseAnalytics.Param.ITEM_ID to event.dto.id,
                            FirebaseAnalytics.Param.ITEM_NAME to event.dto.title,
                            FirebaseAnalytics.Param.CONTENT_TYPE to "product"
                        )
                    )

                    _productDetailsUiState.update {
                        it.copy(productOrServiceItem = event.dto)
                    }
                }

                is MarketPlaceEvent.CategoryDetails -> {
                    _searchQuery.value = ""
                    _selectedCategory.value = event.categoryName


                    _detailsUiState.update {
                        it.copy(categoryItemDto = CategoryItemDto(categoryName = event.categoryName))
                    }

                    firebaseAnalytics.logEvent(
                        FirebaseAnalytics.Event.VIEW_ITEM,
                        bundleOf(
                            FirebaseAnalytics.Param.ITEM_ID to event.categoryName,
                            FirebaseAnalytics.Param.ITEM_NAME to event.categoryName,
                            FirebaseAnalytics.Param.CONTENT_TYPE to "category"
                        )
                    )
                }

                is MarketPlaceEvent.OrderDetails -> {
                    val selectedOrder = _orderScreenUiState.value.orderDetails?.firstOrNull { od ->
                        od.orderId == event.orderId
                    }

                    if (selectedOrder == null) return@launch

                    val paymentRequestsByCommodityId =
                        paymentService.loadRequestsByCommodityId(selectedOrder.orderId).getOrNull()

                    Napie.d("PaymentRequestsByCommodityId: ${selectedOrder.orderId} $paymentRequestsByCommodityId")

                    _orderScreenUiState.update {
                        it.copy(
                            selectedOrder = selectedOrder,
                            paymentInfo = paymentRequestsByCommodityId
                        )
                    }
                }

                is MarketPlaceEvent.ClearCategorySelection -> {
                    _selectedCategory.value = null
                    _searchQuery.value = ""

                    _detailsUiState.update {
                        DetailsUiState() // hard reset
                    }
                }

                else -> Unit
            }
        }
    }
}
