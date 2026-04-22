package com.farmconnect.admin.core.domain.service

interface AuthenticationService {

    val collectionName: String get() = "com:farmconnect/users_v2/metadata"

    val isEmailVerified : Boolean
    val currentUser: com.farmconnect.admin.core.domain.model.AppUser?
    suspend fun signUp(appUser: com.farmconnect.admin.core.domain.model.AppUser.Builder, password: String): Result<com.farmconnect.admin.core.domain.model.AppUser?>


    suspend fun signInWithPhoneNumberRequestCode(phoneNumber: String): Result<Boolean>
    suspend fun signInWithPhoneNumberVerifyCode(code: String): Result<Boolean>
    suspend fun signIn(userEmail: String, userPassword: String): Result<com.farmconnect.admin.core.domain.model.AppUser?>



    fun signOut(/*onComplete: ((Boolean) -> Unit)?*/): Result<Boolean>

    suspend fun verifyEmail(userId : String)


    suspend fun resetPasswordWithEmail(email: String) : Result<Boolean>
}