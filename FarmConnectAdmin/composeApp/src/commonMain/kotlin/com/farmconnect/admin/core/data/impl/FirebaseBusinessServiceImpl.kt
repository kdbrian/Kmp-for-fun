package com.farmconnect.admin.core.data.impl

import com.farmconnect.admin.core.domain.dto.BusinessAccountDto
import com.farmconnect.admin.core.domain.model.BusinessAccount
import com.farmconnect.admin.core.domain.service.BusinessService
import com.farmconnect.admin.core.domain.service.OrderService
import com.farmconnect.admin.core.domain.service.ProductService
import com.farmconnect.admin.core.util.await
import com.farmconnect.admin.core.util.dispatchIOResult
import com.farmconnect.datastore.AppDatastore
import com.farmconnect.datastore.BusinessDatastore
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import Napie.log.Napie

class FirebaseBusinessServiceImpl(
    private val productService: ProductService,
    private val orderService: OrderService,
    private val appDatastore: AppDatastore,
    private val businessDatastore: BusinessDatastore
) : BusinessService {

    override val collectionName: String
        get() = "${BuildConfig.ref_base}_${BuildConfig.db_env}/businesses/metadata"
    private val firestore = FirebaseFirestore.getInstance()


    private val collectionReference = firestore.collection(collectionName)

    override suspend fun businessAccountExistsByName(businessName: String): Result<Boolean> =
        withContext(
            Dispatchers.IO
        ) {
            try {
                val documentSnapshots = collectionReference
                    .whereEqualTo("businessName", businessName)
                    .get().await()
                Result.success(!documentSnapshots.isEmpty)
            } catch (e: Exception) {
                Result.failure(e)
            }

        }

    override suspend fun loadBusinessAccountForUser(userId: String): Result<BusinessAccount> =
        withContext(
            Dispatchers.IO
        ) {
            try {
                val documentSnapshots = collectionReference
                    .whereEqualTo("ownerId", userId)
                    .limit(1L)
                    .get()
                    .await()
                Napie.d("account ${documentSnapshots.isEmpty}")
                if (documentSnapshots.isEmpty)
                    Result.failure(Exception("No business account linked."))
                else {
                    val businessAccount =
                        documentSnapshots.first().toObject(BusinessAccount::class.java)
                    Napie.d("account $businessAccount")
                    businessDatastore.apply {
                        setBusinessId(businessAccount.businessId)
                        setBusinessName(businessAccount.businessName)
                        setLastUpdate(System.currentTimeMillis())
                    }
                    Result.success(businessAccount)
                }
            } catch (e: Exception) {
                Napie.d("failed due to ${e.message}")
                Result.failure(e)
            }
        }

    override suspend fun loadBusinessAccount(businessId: String): Result<BusinessAccount> =
        dispatchIOResult {

            val documentSnapshot =
                collectionReference.document(businessId).get().await()
            val businessAccount = documentSnapshot.toObject(BusinessAccount::class.java)
            Napie.tag("business").d("account $businessAccount")
            businessAccount

        }

    override suspend fun addBusinessAccount(businessAccountDto: BusinessAccountDto): Result<Boolean> =
        dispatchIOResult {
            val documentReference = collectionReference.document()
            val businessAccount = BusinessAccount
                .Builder()
                .fromDto(businessAccountDto)
                .ownerId(appDatastore.uid())
                .build()
            Napie.d("Savin ${businessAccount.businessName} $businessAccount")
            documentReference.set(businessAccount).await()
            true
        }

    override suspend fun updateBusinessAccount(
        businessId: String,
        builder: BusinessAccount.Builder
    ) = withContext(Dispatchers.IO) {
        try {
            val documentReference =
                collectionReference.document(businessId)
            val documentSnapshot = documentReference.get().await()
            if (!documentSnapshot.exists() || documentSnapshot == null) {
                Result.failure(Exception("Business account not found"))
            } else {
                documentReference.set(
                    builder
                        .businessUpdatedOn(System.currentTimeMillis()).build(),
                    SetOptions.merge()
                ).await()
                Result.success(true)
            }


        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteBusinessAccount(businessId: String): Result<Boolean> = withContext(
        Dispatchers.IO
    ) {
        try {
            val documentReference = collectionReference.document(businessId)
            val documentSnapshot = documentReference.get().await()

            if (!documentSnapshot.exists() || documentSnapshot == null) {
                Result.failure(Exception("Business account not found"))
            } else {
                documentReference.delete().await()
                Result.success(true)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun reloadAccount(businessId: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val documentReference = collectionReference.document(businessId)
                val businessAccounts = documentReference.get().await()
                if (businessAccounts.exists() && businessAccounts.data != null) {
                    val businessAccount = businessAccounts.toObject(BusinessAccount::class.java)
                    if (businessAccount != null) {
                        val products = productService.listProducts().await().orEmpty().filter {
                            it.metadata.isNotEmpty() &&
                                    it.metadata.containsKey("business") &&
                                    it.metadata["business"] == businessId
                        }

                        val orders = orderService.getAllOrders().first()
                            .await()
                            .orEmpty()
                            .filter {
                                it.metadata.isNotEmpty() &&
                                        it.metadata.containsKey("business") &&
                                        it.metadata["business"] == businessId
                            }

                        val build = BusinessAccount
                            .Builder()
                            .fromDto(businessAccount.toDto())
                            .businessOrders(orders.size.toLong())
                            .businessProducts(products.size.toLong())
                            .businessUpdatedOn(System.currentTimeMillis())
                            .build()

                        documentReference.set(
                            build,
                            SetOptions.merge()
                        ).await()

                        Result.success(true)

                    } else
                        Result.failure(Exception("Business account not found"))


                } else
                    Result.failure(Exception("Business account not found"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

}