package com.farmconnect.admin.features.auth.usecases

import com.farmconnect.admin.core.domain.service.AuthenticationService
import com.farmconnect.admin.core.util.Resource
import com.farmconnect.admin.core.util.StringUtils
import kotlinx.coroutines.flow.flow

class RequestOtpCodeUseCase(
    private val authenticationService: AuthenticationService
) {

    fun invoke(phoneNumber: String) = flow {
        emit(Resource.Loading())

        if (StringUtils.kenyanRegex.matches(phoneNumber).not())
            emit(Resource.Error("Invalid number format."))


        authenticationService.signInWithPhoneNumberRequestCode(phoneNumber).fold(
            onSuccess = {
                emit(Resource.Success(true))
            },
            onFailure = {
                emit(Resource.Error(it.message.toString()))
            }
        )
    }
}