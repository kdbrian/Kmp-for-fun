package com.farmconnect.admin.features.auth.usecases

import com.farmconnect.admin.core.domain.model.ContactDetailsDomain
import com.farmconnect.admin.core.domain.service.ContactDetailsService

class SaveContactInfoUseCase(
    private val contactDetailsService: ContactDetailsService
) {
   suspend operator fun invoke(
        contactDetailsDomain: ContactDetailsDomain
    ) {
        contactDetailsService.saveDetails(contactDetailsDomain)
    }
}