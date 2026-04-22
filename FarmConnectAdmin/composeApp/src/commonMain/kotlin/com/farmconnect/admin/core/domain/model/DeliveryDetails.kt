package com.farmconnect.admin.core.domain.model

import kotlin.time.Instant

class LocationInfo internal constructor(
    val lat: Long? = null,
    val lng: Long? = null,
    val locationName: String? = null,
){
    class Builder{
        private var lat: Long? = null
        private var lng: Long? = null
        private var locationName: String? = null

        fun setLat(lat: Long) = apply { this.lat = lat }
        fun setLng(lng: Long) = apply { this.lng = lng }
        fun setLocationName(locationName: String) = apply { this.locationName = locationName }

        fun build() = _root_ide_package_.com.farmconnect.admin.core.domain.model.LocationInfo(
            lat,
            lng,
            locationName
        )
    }
}

class ContactInfo internal constructor(
    val phone: String? = null,
    val email: String? = null
) {
    class Builder {
        private var phone: String? = null
        private var email: String? = null

        fun setPhone(phone: String) = apply { this.phone = phone }
        fun setEmail(email: String) = apply { this.email = email }

        fun build() = _root_ide_package_.com.farmconnect.admin.core.domain.model.ContactInfo(phone, email)
    }
}

interface DeliveryDetails {
    val name: String?
        get() = null

    val locationInfo: com.farmconnect.admin.core.domain.model.LocationInfo?
        get() = null

    val contactInfo: com.farmconnect.admin.core.domain.model.ContactInfo?
        get() = null

    val updatedOn: Long
        get() = Instant.now().epochSeconds
}