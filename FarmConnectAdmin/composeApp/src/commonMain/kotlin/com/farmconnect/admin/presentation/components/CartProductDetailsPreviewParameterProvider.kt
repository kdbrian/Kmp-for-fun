package com.farmconnect.core.ui.components

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.farmconnect.core.ui.R
import com.farmconnect.core.ui.components.cart.CartProductPreviewData

class CartProductDetailsPreviewParameterProvider :
    PreviewParameterProvider<CartProductPreviewData> {
    override val values: Sequence<CartProductPreviewData>
        get() {
            return sequenceOf(
                demoProduct,
                CartProductPreviewData(
                    placeholder = R.drawable.placeholder,
                    title = "Organic Whole Grain Oats",
                    price = 12.99,
                    discount = null
                ),
                CartProductPreviewData(
                    placeholder = R.drawable.placeholder,
                    title = "Premium Coffee Beans",
                    price = 25.00,
                    discount = 5.0
                ),
                CartProductPreviewData(
                    placeholder = R.drawable.placeholder,
                    title = "Fresh Handpicked Apples",
                    price = 0.99,
                    productAmount = 5,
                    discount = null
                ),
                CartProductPreviewData(
                    placeholder = R.drawable.placeholder,
                    title = LoremIpsum(12).values.joinToString(),
                    price = 0.99,
                    productAmount = 5,
                    discount = null
                )
            )
        }
}