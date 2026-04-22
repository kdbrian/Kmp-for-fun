package com.farmconnect.core.ui.components.cart

import com.farmconnect.core.ui.R
import com.farmconnect.core.util.UnitType

data class CartProductPreviewData(
    val imageUrl: String? = "",
    val productAmount: Int = 1,
    val placeholder: Int = R.drawable.placeholder,
    val title: String,
    val price: Double,
    val unit : UnitType = UnitType.KILOGRAMS,
    val discount: Double? = null
)