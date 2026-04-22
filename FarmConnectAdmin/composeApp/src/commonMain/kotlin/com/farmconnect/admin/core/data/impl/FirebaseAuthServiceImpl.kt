package com.farmconnect.admin.core.data.impl

import android.app.Activity
import com.farmconnect.admin.core.domain.model.AppUser
import com.farmconnect.admin.core.domain.model.toAppUser
import com.farmconnect.admin.core.domain.service.AppUserService
import com.farmconnect.admin.core.domain.service.AuthenticationService
import com.farmconnect.admin.core.util.await
import com.farmconnect.admin.core.util.dispatchIO
import com.farmconnect.admin.core.util.dispatchIOResult
Exception
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.tasks.await
import Napie.log.Napier
import java.util.concurrent.TimeUnit


class FirebaseAuthServiceImpl(
    private val activity: Activity,
    private val appUserService: AppUserService
) : AuthenticationService {

    override val collectionName: String
        get() = "${BuildConfig.ref_base}_${BuildConfig.db_env}/users/metadata"
    private val auth = FirebaseAuth.getInstance()

    override val currentUser: AppUser?
        get() = auth.currentUser?.toAppUser()?.build()

    private val firestore = FirebaseFirestore.getInstance()


    override val isEmailVerified: Boolean
        get() = auth.currentUser?.isEmailVerified == true

    private var currentVerificationId: String? = null
    private var verificationInitiationDeferred: CompletableDeferred<Result<Boolean>>? = null

    override suspend fun signInWithPhoneNumberRequestCode(phoneNumber: String): Result<Boolean> =
        dispatchIO {
            verificationInitiationDeferred = CompletableDeferred()
            currentVerificationId = null

            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        Napie.d("onVerificationCompleted: Auto-verification successful.")
                        auth.signInWithCredential(credential)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Napie.d("onVerificationCompleted: Sign-in with credential successful.")
                                    verificationInitiationDeferred?.complete(Result.success(true))
                                } else {
                                    Napie.e("onVerificationCompleted: Sign-in with credential failed: ${task.exception}")
                                    verificationInitiationDeferred?.complete(
                                        Result.failure(
                                            task.exception
                                                ?: Exception("Unknown error during auto-sign-in")
                                        )
                                    )
                                }
                            }
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        Napie.e(e, "onVerificationFailed: ${e.message}")
                        verificationInitiationDeferred?.complete(Result.failure(e))
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        Napie.d("onCodeSent: Code sent to $phoneNumber. Verification ID: $verificationId")
                        currentVerificationId = verificationId
                        verificationInitiationDeferred?.complete(Result.success(true))
                    }
                }).build()

            PhoneAuthProvider.verifyPhoneNumber(options)

            verificationInitiationDeferred!!.await()
        }

    override suspend fun signInWithPhoneNumberVerifyCode(code: String): Result<Boolean> =
        dispatchIOResult {
            val verificationId = currentVerificationId
            verificationId?.let {
                val credential = PhoneAuthProvider.getCredential(verificationId, code)
                val authResult = auth.signInWithCredential(credential).await()
                Napie.d("verifyCode: Sign-in with credential successful. User phone: ${authResult.user?.phoneNumber}")
                authResult.user != null
            }
        }

    override suspend fun signUp(
        appUser: AppUser.Builder,
        password: String
    ): Result<AppUser?> = dispatchIOResult {
        val build = appUser.build()
        Napie.d("user $build")

        val exists =
            appUserService.userExistWithEmailOrPhoneNumber(build.email, build.phone).await() ?: false
        Napie.d("Exists : $exists")

        if (!exists) {
            val authResult = auth
                .createUserWithEmailAndPassword(build.email.toString(), password)
                .await()

            authResult.user?.toAppUser()?.let { userBuilder ->
                // ✅ Build the complete AppUser with all fields
                val newAppUser = userBuilder
                    .setPhone(build.phone)
                    .setEmail(build.email)
                    .setUid(authResult.user!!.uid)
                    .setModes(build.modes)
                    .build()

                Napie.d("user: $newAppUser")

                firestore
                    .collection(collectionName)
                    .document(newAppUser.uid)
                    .set(newAppUser) // ✅ Save the fully built object, not the raw builder
                    .addOnFailureListener { exception ->
                        Napie.tag("signUp").d(exception.message.toString())
                        signOut()
                    }
                    .await()

                // ✅ Unwrap the Result so the expression returns AppUser?
                appUserService.profileById(newAppUser.uid).await()
            }
        } else {
            auth.signOut()
            null
        }
    }

    override suspend fun signIn(userEmail: String, userPassword: String): Result<AppUser?> =
        dispatchIOResult {
            auth.signOut()
            val authResult = auth.signInWithEmailAndPassword(userEmail, userPassword).await()

            authResult.user?.uid?.let {
                val appUser = appUserService.profileById(it).await()
                Napie.d("user: $appUser")
                appUser
            }
        }

    override fun signOut(): Result<Boolean> {
        auth.signOut()
        return Result.success(auth.currentUser == null)
    }

    override suspend fun verifyEmail(userId: String) = dispatchIO {
        appUserService.profileById(userId).let { _ ->
            auth.currentUser?.sendEmailVerification()?.await()
        }
        Unit
    }


    override suspend fun resetPasswordWithEmail(email: String): Result<Boolean> =
        dispatchIOResult {
            auth.sendPasswordResetEmail(email).await()
            true
        }


}