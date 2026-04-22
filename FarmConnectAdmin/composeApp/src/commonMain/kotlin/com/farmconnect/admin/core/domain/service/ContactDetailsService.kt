package com.farmconnect.admin.core.domain.service

import kotlinx.coroutines.flow.Flow

interface ContactDetailsService {

    suspend fun loadDetails() : Flow<List<com.farmconnect.admin.core.domain.model.ContactDetailsDomain>>

    suspend fun saveDetails(details: com.farmconnect.admin.core.domain.model.ContactDetails)

    suspend fun deleteDetails(id : String) : Boolean

}