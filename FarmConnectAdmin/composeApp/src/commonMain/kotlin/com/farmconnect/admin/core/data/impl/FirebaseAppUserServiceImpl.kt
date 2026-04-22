package com.farmconnect.admin.core.data.impl

import com.farmconnect.admin.core.domain.model.AppUser
import com.farmconnect.admin.core.domain.service.AppUserService
import com.farmconnect.admin.core.util.dispatchIOResult
import com.farmconnect.datastore.AppDatastore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import Napie.log.Napie

class FirebaseAppUserServiceImpl(
    firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val appDatastore: AppDatastore,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : AppUserService {


    override val isEmailVerified: Boolean
        get() = auth.currentUser?.isEmailVerified == true


    override val collectionName: String
        get() = "${BuildConfig.ref_base}_${BuildConfig.db_env}/users/metadata"
    private val collectionReference = firestore.collection(collectionName)

    override val currentUser: Flow<AppUser?>
        get() = flow {
            profileById(appDatastore.uid()).fold(
                onSuccess = {
                    it?.let {
                        appDatastore.setAppUser(it)
                        emit(it)
                    }
                },
                onFailure = { emit(null) }
            )
        }


    override suspend fun updateAccount(
        id: String,
        updated: Map<String, Any>
    ): Result<Boolean> = dispatchIOResult {
        val account = profileById(id).getOrNull()

        Napier.d("Updating: $account $updated")

        collectionReference
            .document(id)
            .set(updated.toMutableMap().apply {
                put("updatedOn", System.currentTimeMillis())
            }, SetOptions.merge())
            .await()
        true
    }


    override suspend fun profileById(id: String): Result<AppUser?> = dispatchIOResult {
        val account = collectionReference.document(id).get().await()
        account.toObject(AppUser::class.java)
    }


    override suspend fun userExistWithEmailOrPhoneNumber(
        email: String?,
        phoneNumber: String?
    ): Result<Boolean> = dispatchIOResult {
        val snapshots = collectionReference
            .get().await().toObjects(AppUser::class.java)

        // ✅ OR condition — match either email or phone, only comparing non-null inputs
        val appUsers = snapshots.filter { user ->
            (email != null && user.email == email) ||
                    (phoneNumber != null && user.phone == phoneNumber)
        }

        Napier.d("user ${appUsers.size} $appUsers")
        appUsers.isNotEmpty()
    }

    override suspend fun updateName(name: String): Result<Boolean> = dispatchIOResult {
        val userAvailable = auth.currentUser != null
        if (userAvailable && name.isNotEmpty()) {
            auth.currentUser?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(name).build()
            )

            collectionReference
                .document(auth.currentUser!!.uid)
                .update(mapOf("metadata.displayName" to name))
                .await()
        }
        userAvailable
    }


}