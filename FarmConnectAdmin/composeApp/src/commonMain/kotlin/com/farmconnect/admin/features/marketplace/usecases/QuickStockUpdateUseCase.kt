package com.farmconnect.admin.features.marketplace.usecases

import com.farmconnect.admin.core.domain.service.ProductService

class QuickStockUpdateUseCase(
    private val productService: ProductService,
) {
    suspend operator fun invoke(
        productId: String,
        newStock: Int
    ) {
        productService.updateProduct(
            productId,
            mapOf("stock" to newStock)
        )
    }
}