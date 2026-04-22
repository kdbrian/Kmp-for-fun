package com.farmconnect.admin.features.auth.usecases

import com.farmconnect.admin.core.domain.service.ContactDetailsService

class DeleteContactInfoByIdUseCase(
    private val contactDetailsService: ContactDetailsService
){

    suspend operator fun invoke(id : String) = contactDetailsService.deleteDetails(id)

}