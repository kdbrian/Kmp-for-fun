package com.farmconnect.admin.core.domain.model

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
class BusinessAccount internal constructor(
    val ownerId: String = "",
    val businessId: String = "",
    val businessName: String = "",
    val businessDescription: String = "",
    val businessEmail: String = "",
    val businessPhone: String = "",
    val businessAddress: String = "",
    val businessLogo: String = "",
    val businessCover: String = "",
    val businessCategory: String = "",
    val businessType: String = "",
    val businessStatus: String = "",
    val businessVerified: Boolean = false,
    val rating: Double = 0.0,
    val ratingCount: Int = 0,
    val products: Long = 0,
    val orders: Long = 0,
    val createdOn: Long = Instant.now().epochSeconds,
    val updatedOn: Long = Instant.now().epochSeconds,
) {
    fun toDto() = _root_ide_package_.com.farmconnect.admin.core.domain.dto.BusinessAccountDto.Builder()
        .apply {
            _root_ide_package_.com.farmconnect.admin.core.domain.dto.BusinessAccountDto.Builder.ownerId(
                ownerId
            )
            _root_ide_package_.com.farmconnect.admin.core.domain.dto.BusinessAccountDto.Builder.businessId(
                businessId
            )
            _root_ide_package_.com.farmconnect.admin.core.domain.dto.BusinessAccountDto.Builder.businessName(
                businessName
            )
            _root_ide_package_.com.farmconnect.admin.core.domain.dto.BusinessAccountDto.Builder.businessDescription(
                businessDescription
            )
            _root_ide_package_.com.farmconnect.admin.core.domain.dto.BusinessAccountDto.Builder.businessEmail(
                businessEmail
            )
            _root_ide_package_.com.farmconnect.admin.core.domain.dto.BusinessAccountDto.Builder.businessPhone(
                businessPhone
            )
            _root_ide_package_.com.farmconnect.admin.core.domain.dto.BusinessAccountDto.Builder.businessAddress(
                businessAddress
            )
            _root_ide_package_.com.farmconnect.admin.core.domain.dto.BusinessAccountDto.Builder.businessLogo(
                businessLogo
            )
            _root_ide_package_.com.farmconnect.admin.core.domain.dto.BusinessAccountDto.Builder.businessCover(
                businessCover
            )
            _root_ide_package_.com.farmconnect.admin.core.domain.dto.BusinessAccountDto.Builder.businessCategory(
                businessCategory
            )
            _root_ide_package_.com.farmconnect.admin.core.domain.dto.BusinessAccountDto.Builder.businessType(
                businessType
            )
            _root_ide_package_.com.farmconnect.admin.core.domain.dto.BusinessAccountDto.Builder.businessStatus(
                businessStatus
            )
            _root_ide_package_.com.farmconnect.admin.core.domain.dto.BusinessAccountDto.Builder.businessVerified(
                businessVerified
            )
            _root_ide_package_.com.farmconnect.admin.core.domain.dto.BusinessAccountDto.Builder.rating(
                rating
            )
            _root_ide_package_.com.farmconnect.admin.core.domain.dto.BusinessAccountDto.Builder.ratingCount(
                ratingCount
            )

            _root_ide_package_.com.farmconnect.admin.core.domain.dto.BusinessAccountDto.Builder.createdOn(
                createdOn
            )
            _root_ide_package_.com.farmconnect.admin.core.domain.dto.BusinessAccountDto.Builder.updatedOn(
                updatedOn
            )
            _root_ide_package_.com.farmconnect.admin.core.domain.dto.BusinessAccountDto.Builder.products(
                products
            )
            _root_ide_package_.com.farmconnect.admin.core.domain.dto.BusinessAccountDto.Builder.orders(
                orders
            )
        }
        .build()

    class Builder {

        private var ownerId: String = ""
        private var businessId: String = ""
        private var businessName: String = ""
        private var businessDescription: String = ""
        private var businessEmail: String = ""
        private var businessPhone: String = ""
        private var businessAddress: String = ""
        private var businessLogo: String = ""
        private var businessCover: String = ""
        private var businessCategory: String = ""
        private var businessType: String = ""
        private var businessStatus: String = ""
        private var businessVerified: Boolean = false
        private var rating: Double = 0.0
        private var ratingCount: Int = 0
        private var products: Long = 0
        private var orders: Long = 0
        private var createdOn: Long = Instant.now().epochSeconds
        private var updatedOn: Long = Instant.now().epochSeconds

        fun ownerId(ownerId: String) = apply { this.ownerId = ownerId }
        fun businessId(businessId: String) = apply { this.businessId = businessId }
        fun businessName(businessName: String) = apply { this.businessName = businessName }
        fun businessDescription(businessDescription: String) =
            apply { this.businessDescription = businessDescription }

        fun businessEmail(businessEmail: String) = apply { this.businessEmail = businessEmail }
        fun businessPhone(businessPhone: String) = apply { this.businessPhone = businessPhone }
        fun businessAddress(businessAddress: String) =
            apply { this.businessAddress = businessAddress }

        fun businessLogo(businessLogo: String) = apply { this.businessLogo = businessLogo }
        fun businessCover(businessCover: String) = apply { this.businessCover = businessCover }
        fun businessCategory(businessCategory: String) =
            apply { this.businessCategory = businessCategory }

        fun businessType(businessType: String) = apply { this.businessType = businessType }
        fun businessStatus(businessStatus: String) = apply { this.businessStatus = businessStatus }
        fun businessVerified(businessVerified: Boolean) =
            apply { this.businessVerified = businessVerified }

        fun businessRating(rating: Double) = apply { this.rating = rating }
        fun businessRatingCount(ratingCount: Int) = apply { this.ratingCount = ratingCount }
        fun businessCreatedOn(createdOn: Long) = apply { this.createdOn = createdOn }
        fun businessUpdatedOn(updatedOn: Long) = apply { this.updatedOn = updatedOn }
        fun businessProducts(products: Long) = apply { this.products = products }
        fun businessOrders(orders: Long) = apply { this.orders = orders }


        fun fromDto(dto: com.farmconnect.admin.core.domain.dto.BusinessAccountDto) = apply {
            ownerId=dto.ownerId
            businessId = dto.businessId
            businessName = dto.businessName
            businessDescription = dto.businessDescription
            businessEmail = dto.businessEmail
            businessPhone = dto.businessPhone
            businessAddress = dto.businessAddress
            businessLogo = dto.businessLogo
            businessCover = dto.businessCover
            businessCategory = dto.businessCategory
            businessType = dto.businessType
            businessStatus = dto.businessStatus
            businessVerified = dto.businessVerified
            rating = dto.rating
            ratingCount = dto.ratingCount
            createdOn = dto.createdOn
            updatedOn = dto.updatedOn
            products = dto.products
            orders = dto.orders
        }

        fun build() = _root_ide_package_.com.farmconnect.admin.core.domain.model.BusinessAccount(
            ownerId = ownerId,
            businessId = businessId,
            businessName = businessName,
            businessDescription = businessDescription,
            businessEmail = businessEmail,
            businessPhone = businessPhone,
            businessAddress = businessAddress,
            businessLogo = businessLogo,
            businessCover = businessCover,
            businessCategory = businessCategory,
            businessType = businessType,
            businessStatus = businessStatus,
            businessVerified = businessVerified,
            rating = rating,
            ratingCount = ratingCount,
            createdOn = createdOn,
            updatedOn = updatedOn,
        )

    }
}
