package com.farmconnect.admin.core.data.impl

import com.farmconnect.admin.core.domain.dto.PaymentUtils
import com.farmconnect.admin.core.domain.service.PaymentService
import com.farmconnect.admin.core.util.safeApiCall



import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import Napie.log.Napie

class FirebasePaymentService(
    private val client: HttpClient
) : PaymentService {

    private val rtdb = Firebase.database
    override val dbRef: String
        get() = "${BuildConfig.ref_base}_${BuildConfig.db_env}/payments/metadata/"

    private val reference = rtdb.getReference(dbRef)

    override suspend fun requestsMpesaPayment(ref: String): Result<PaymentUtils.MpesaPaymentResponse> =
        safeApiCall{
                val response = client.get(BuildConfig.requestPaymentUrl) {
                    parameter("paymentId", ref)
                }
                val result = response.bodyAsText()
                val resultBody =
                    Json.decodeFromString<PaymentUtils.MpesaPaymentResponse>(response.bodyAsText())
                Napie.d("result:$result")
                resultBody
        }

    override suspend fun loadRequestsByCommodityId(id: String): Result<PaymentUtils.MPesaPaymentRecord?> {
        return try {
            Napie.d("loading comm $id")

            val dataSnapshot = reference
                .orderByChild("commodity")
                .equalTo(id)
                .limitToFirst(1)
                .get()
                .await()

            Napie.d("loading comm exists=${dataSnapshot.exists()} count=${dataSnapshot.childrenCount}")

            if (!dataSnapshot.exists()) {
                Result.success(null)
            } else {
                val child = dataSnapshot.children.firstOrNull()
                val record = child?.getValue(PaymentUtils.MPesaPaymentRecord::class.java)

                Napie.d("loading comm $record")
                Result.success(record)
            }
        } catch (e: Exception) {
            Napie.d("loading comm failed ${e.message}")
            Result.failure(e)
        }
    }


    override suspend fun loadRequestsBySubject(subject: String): Flow<List<PaymentUtils.MPesaPaymentRecord>> =
        reference.snapshots.map { dataSnapshot ->
            dataSnapshot.children.mapNotNull { it.getValue(PaymentUtils.MPesaPaymentRecord::class.java) }
        }.catch {
            emit(emptyList())
        }

    override suspend fun savePaymentRequest(payment: PaymentUtils.MPesaPaymentRecord.Builder): Result<String> {
        val child = reference.push()
        child.setValue(
            payment.id(child.key.toString()).build()
        ).await()
        return Result.success(child.key.toString())
    }

    override suspend fun updatePaymentRequest(
        id: String, updates: Map<String, Any>
    ): Result<String> {
        reference.child(id).updateChildren(updates).await()
        return Result.success(id)
    }
}