package com.farmconnect.admin.core.domain.model

import java.util.UUID
import kotlin.time.Instant

interface ProductTemplate {
    val templateId: String
        get() = UUID.randomUUID().toString().split("-").first()
    val productName: String
        get() = ""
    val productDescription: String
        get() = ""
    val productAveragePrice: Double
        get() = 0.0
    val addedBy: String
        get() = ""
    val usedBy: List<String>
        get() = emptyList()
    val categories: List<String>
        get() = emptyList()
    val reviews: List<String>
        get() = emptyList()
    val addedOn: Long
        get() = Instant.now().epochSeconds
    val updatedOn: Long
        get() = Instant.now().epochSeconds

//    val isFavourite: Boolean
//        get() = false
}

fun com.farmconnect.admin.core.domain.model.ProductTemplate.toDto() =
    _root_ide_package_.com.farmconnect.admin.core.domain.dto.ProductTemplateDto(
        templateId = _root_ide_package_.com.farmconnect.admin.core.domain.model.ProductTemplate.templateId,
        productName = _root_ide_package_.com.farmconnect.admin.core.domain.model.ProductTemplate.productName,
        productDescription = _root_ide_package_.com.farmconnect.admin.core.domain.model.ProductTemplate.productDescription,
        productAveragePrice = _root_ide_package_.com.farmconnect.admin.core.domain.model.ProductTemplate.productAveragePrice,
        addedBy = _root_ide_package_.com.farmconnect.admin.core.domain.model.ProductTemplate.addedBy,
        usedBy = _root_ide_package_.com.farmconnect.admin.core.domain.model.ProductTemplate.usedBy,
        addedOn = _root_ide_package_.com.farmconnect.admin.core.domain.model.ProductTemplate.addedOn,
        updatedOn = _root_ide_package_.com.farmconnect.admin.core.domain.model.ProductTemplate.updatedOn,
        categories = _root_ide_package_.com.farmconnect.admin.core.domain.model.ProductTemplate.categories,
        reviews = _root_ide_package_.com.farmconnect.admin.core.domain.model.ProductTemplate.reviews
    )
