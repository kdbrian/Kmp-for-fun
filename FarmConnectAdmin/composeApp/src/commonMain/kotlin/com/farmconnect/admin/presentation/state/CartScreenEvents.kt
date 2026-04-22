package com.farmconnect.admin.presentation.state

import com.farmconnect.admin.core.domain.dto.ProductOrServiceItemDto

interface CartScreenEvents : MarketPlaceEvent {
    data object ClearCart : CartScreenEvents
    data class ViewProduct(val productOrServiceItemDto: ProductOrServiceItemDto) : CartScreenEvents
    data class AddItemToCart(val productOrServiceItemDto: ProductOrServiceItemDto) : CartScreenEvents
    data class UpdateItemCount(val productOrServiceItemDto: ProductOrServiceItemDto, val count : Int) : CartScreenEvents

    data object MoveToCheckout : CartScreenEvents

}