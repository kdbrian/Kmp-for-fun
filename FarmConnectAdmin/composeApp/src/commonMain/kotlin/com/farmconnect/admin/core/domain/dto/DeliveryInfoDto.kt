package com.farmconnect.admin.core.domain.dto

class DeliveryInfoDto internal constructor(
    override val contactInfo: com.farmconnect.admin.core.domain.model.ContactInfo?,
    override val locationInfo: com.farmconnect.admin.core.domain.model.LocationInfo?,
    override val name: String?,
    override val updatedOn: Long
) : com.farmconnect.admin.core.domain.model.DeliveryDetails {

    constructor(
        builder: Builder
    ) : this(
        builder.contactInfo?.build(),
        builder.locationInfo?.build(),
        builder.name,
        builder.updatedOn
    )


    class Builder {

        var contactInfo: com.farmconnect.admin.core.domain.model.ContactInfo.Builder? = null
        var locationInfo: com.farmconnect.admin.core.domain.model.LocationInfo.Builder? = null
        var name: String? = null
        var updatedOn: Long = 0

        fun setContactInfo(contactInfo: com.farmconnect.admin.core.domain.model.ContactInfo.Builder): Builder = apply {
            this.contactInfo = contactInfo
        }

        fun setLocationInfo(locationInfo: com.farmconnect.admin.core.domain.model.LocationInfo.Builder): Builder = apply {
            this.locationInfo = locationInfo
        }

        fun setName(name: String): Builder = apply {
            this.name = name
        }


        fun setUpdatedOn(updatedOn: Long): Builder = apply {
            this.updatedOn = updatedOn
        }

    }


}