package com.farmconnect.admin.features.auth.usecases

import com.farmconnect.admin.core.domain.service.AppUserService
import com.farmconnect.admin.core.domain.service.AuthenticationService
import com.farmconnect.admin.core.util.Resource
import kotlinx.coroutines.flow.flow

class VerifyOtpCodeUseCase(
    private val authenticationService: AuthenticationService,
    private val appUserService: AppUserService
) {

    fun invoke(phoneNumber: String, code: String) = flow {
        emit(Resource.Loading())

        authenticationService.signInWithPhoneNumberVerifyCode(
            code
        ).fold(onSuccess = {
            if (it) {
                appUserService.updateAccount(
                    authenticationService.currentUser?.uid.toString(),
                    mapOf(
                        "verifiedId" to "${System.currentTimeMillis()}#$phoneNumber#${authenticationService.currentUser?.uid}",
                        "updatedOn" to System.currentTimeMillis()
                    )
                ).fold(
                    onSuccess = {
                        emit(Resource.Success(true))
                    },
                    onFailure = {
                        emit(Resource.Error(it.message.toString()))
                    }
                )
            }
        }, onFailure = {
            emit(Resource.Idle())
        })
    }
}