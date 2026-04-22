package com.farmconnect.admin.core.domain.dto

import kotlin.time.Instant

class BusinessAccountDto internal constructor(
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
    val products : Long = 0,
    val orders : Long = 0,
    val createdOn: Long = Instant.now().epochSeconds,
    val updatedOn: Long = Instant.now().epochSeconds,
){
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

        private var rating: Double = 0.0
        private var ratingCount: Int = 0
        private var products : Long = 0
        private var orders : Long = 0

        private var createdOn: Long = Instant.now().epochSeconds
        private var updatedOn: Long = Instant.now().epochSeconds
        private var businessVerified: Boolean = false

        fun businessVerified(businessVerified: Boolean) = apply { this.businessVerified = businessVerified }

        fun rating(rating: Double) = apply { this.rating = rating }
        fun ownerId(ownerId: String) = apply { this.ownerId = ownerId }

        fun ratingCount(ratingCount: Int) = apply { this.ratingCount = ratingCount }

        fun createdOn(createdOn: Long) = apply { this.createdOn = createdOn }

        fun updatedOn(updatedOn: Long) = apply { this.updatedOn = updatedOn }

        fun businessId(businessId: String) = apply { this.businessId = businessId }
        fun businessName(businessName: String) = apply { this.businessName = businessName }
        fun businessDescription(businessDescription: String) = apply { this.businessDescription = businessDescription }
        fun businessEmail(businessEmail: String) = apply { this.businessEmail = businessEmail }
        fun businessPhone(businessPhone: String) = apply { this.businessPhone = businessPhone }
        fun businessAddress(businessAddress: String) = apply { this.businessAddress = businessAddress }
        fun businessLogo(businessLogo: String) = apply { this.businessLogo = businessLogo }
        fun businessCover(businessCover: String) = apply { this.businessCover = businessCover }
        fun businessCategory(businessCategory: String) = apply { this.businessCategory = businessCategory }
        fun businessType(businessType: String) = apply { this.businessType = businessType }
        fun businessStatus(businessStatus: String) = apply { this.businessStatus = businessStatus }
        fun products(products: Long) = apply { this.products = products }
        fun orders(orders: Long) = apply { this.orders = orders }

        fun build() = _root_ide_package_.com.farmconnect.admin.core.domain.dto.BusinessAccountDto(
            businessId = businessId,
            businessName = businessName,
            ownerId = ownerId,
            businessDescription = businessDescription,
            businessEmail = businessEmail,
            businessPhone = businessPhone,
            businessAddress = businessAddress,
            businessLogo = businessLogo,
            businessCover = businessCover,
            businessCategory = businessCategory,
            businessType = businessType,
            businessStatus = businessStatus,
            rating = rating,
            ratingCount = ratingCount,
            createdOn = createdOn,
            updatedOn = updatedOn,
            products = products,
            orders = orders,
        )



    }
}
