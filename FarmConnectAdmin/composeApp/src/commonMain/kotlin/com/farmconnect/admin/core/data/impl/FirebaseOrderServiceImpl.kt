package com.farmconnect.admin.core.data.impl

import com.farmconnect.admin.core.domain.dto.OrderDetailsDto
import com.farmconnect.admin.core.domain.dto.toDetails
import com.farmconnect.admin.core.domain.model.OrderDetails
import com.farmconnect.admin.core.domain.service.OrderService
import com.farmconnect.admin.core.util.Status
import com.farmconnect.admin.core.util.dispatchIOResult
import com.farmconnect.admin.core.util.safeApiCall
import com.farmconnect.datastore.AppDatastore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import Napie.log.Napie

class FirebaseOrderServiceImpl(
    private val appDatastore: AppDatastore
) : OrderService {

    override val collectionName: String
        get() = "${BuildConfig.ref_base}_${BuildConfig.db_env}/orders/metadata/"
    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val reference = database.getReference(collectionName)

    override suspend fun placeOrder(orderDto: OrderDetailsDto): Result<String> = safeApiCall {
        val databaseReference = reference.push()
        databaseReference.setValue(
            orderDto.copy(
                orderId = databaseReference.key.toString(),
                placeOn = System.currentTimeMillis(),
                userId = auth.uid.toString(),
                status = Status.Pending.name,
                metadata = orderDto.metadata.toMutableMap().apply {
                    appDatastore.notificationToken()?.let { token ->
                        put("notifyUser", token)
                    }
                }
            ).toDetails()
        ).await()
        databaseReference.key.toString()
    }

    override suspend fun getAllOrders(): Flow<Result<List<OrderDetails>>> =
        reference.snapshots
            .map { dataSnapshot ->
                Result.success(
                    dataSnapshot.children.mapNotNull {
                        it.getValue(OrderDetails::class.java)
                    })
            }
            .catch {
                Napie.d("Err $it")
                emit(Result.failure(it))
            }

    override suspend fun loadOrdersByKey(
        key: String,
        value: String
    ): Flow<Result<List<OrderDetails>>> {
        val userQuery = reference.orderByChild(key).equalTo(value)

        return userQuery.snapshots
            .map { dataSnapshot ->
                val orders = dataSnapshot.children
                    .mapNotNull { childSnapshot ->
                        childSnapshot.getValue<OrderDetails>()
                    }

                Result.success(orders)
            }
            .catch { e ->
                Napie.d("Err $e")
                emit(Result.failure(e))
            }

    }

    override suspend fun loadOrderById(id: String): Result<OrderDetails> = dispatchIOResult {
        val docRef = reference.child(id).get().await()
        docRef.getValue(OrderDetails::class.java)
    }

}