package com.farmconnect.admin.core.domain.model

import kotlinx.serialization.Serializable

interface ContactDetails {
    val id: String?
    val email: String?
    val phoneNumber: String?
    val locationName: String?
    val location: List<Double>?
}

@Serializable
data class ContactDetailsDomain(
    override val location: List<Double>? = null,
    override val locationName: String? = null,
    override val phoneNumber: String? = null,
    override val id: String? = null,
    override val email: String? = null
) : com.farmconnect.admin.core.domain.model.ContactDetails {
    class Builder {
        private var location: List<Double>? = null
        private var locationName: String? = null
        private var phoneNumber: String? = null
        private var id: String? = null
        private var email: String? = null

        fun location(location: List<Double>) = apply { this.location = location }
        fun locationName(locationName: String) = apply { this.locationName = locationName }
        fun phoneNumber(phoneNumber: String) = apply { this.phoneNumber = phoneNumber }
        fun id(id: String) = apply { this.id = id }
        fun email(email: String) = apply { this.email = email }
        fun build() = _root_ide_package_.com.farmconnect.admin.core.domain.model.ContactDetailsDomain(
            location,
            locationName,
            phoneNumber,
            id,
            email
        )

    }
}