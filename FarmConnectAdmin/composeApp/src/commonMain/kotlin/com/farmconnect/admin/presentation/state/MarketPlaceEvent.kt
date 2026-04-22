package com.farmconnect.admin.presentation.state

import com.farmconnect.admin.core.domain.dto.ProductOrServiceItemDto


sealed interface MarketPlaceEvent {
    val name: String? get() = null

    data object OpenProfile : MarketPlaceEvent
    data object OpenNotifications : MarketPlaceEvent
    data object Search : MarketPlaceEvent
    data object Location : MarketPlaceEvent
    data object ReleaseNotes : MarketPlaceEvent

    data object SignOut : MarketPlaceEvent
    data class ViewProduct(val dto: ProductOrServiceItemDto) :
        MarketPlaceEvent

    object ClearCategorySelection : MarketPlaceEvent
    data class OrderDetails(
        val orderId: String
    ) : MarketPlaceEvent

    data object OpenCart : MarketPlaceEvent
    data object OpenOrderHistory : MarketPlaceEvent
    data  object Refresh : MarketPlaceEvent

    data class UpdateProductInCartDescription(
        val productId: String,
        val description: String
    ) : MarketPlaceEvent

    data class CategoryDetails(val categoryName: String, val categoryDescription: String) :
        MarketPlaceEvent

    data class AddProductToCart(val productOrServiceItem: ProductOrServiceItemDto?) :
        MarketPlaceEvent

    data class RemoveProductFromCart(val productId: String) :
        MarketPlaceEvent

    data class ToggleFavouriteProduct(val productId: String) :
        MarketPlaceEvent

    data class ToggleAccount(val mode: AccountType) : MarketPlaceEvent

}