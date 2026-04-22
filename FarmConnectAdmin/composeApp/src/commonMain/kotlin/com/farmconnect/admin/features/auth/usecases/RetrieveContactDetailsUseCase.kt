package com.farmconnect.admin.features.auth.usecases

import com.farmconnect.admin.core.domain.model.ContactDetailsDomain
import com.farmconnect.admin.core.domain.service.ContactDetailsService
import kotlinx.coroutines.flow.Flow

class RetrieveContactDetailsUseCase(
    private val contactDetailsService: ContactDetailsService
) {
    suspend operator fun invoke(): Flow<List<ContactDetailsDomain>> =
        contactDetailsService.loadDetails()

}