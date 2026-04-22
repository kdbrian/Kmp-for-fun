package com.farmconnect.admin.core.data.impl


import FarmConnectAdmin.composeApp.BuildConfig
import com.farmconnect.admin.core.domain.dto.TransactionDto
import com.farmconnect.admin.core.domain.dto.toEntity
import com.farmconnect.admin.core.domain.model.AppTransaction
import com.farmconnect.admin.core.domain.service.AppTransactionService
import com.farmconnect.admin.core.utils.safeApiCall
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class FirebaseAppTransactionService : AppTransactionService {
    override val path: String
        get() = "${BuildConfig.ref_base}_${BuildConfig.db_env}/transactions/metadata"
    private val database = Firebase.database
    private val ref = database.reference(path)


    override suspend fun saveTransaction(transactionDto: TransactionDto): Result<String> =
        safeApiCall {

            val databaseReference = ref.push()
            val transactionId = databaseReference.key.toString()
            databaseReference
                .setValue(transactionDto.toEntity().copy(transactionId = transactionId))

            transactionId
        }

    override suspend fun loadTransactionsFor(transactionFor: String): Flow<Result<List<AppTransaction>>> =
        ref
            .child("")
            .map { snapshot ->
//                Napier.d("snap [${snapshot.ref.key} ${snapshot.value}]")
                val appTransactions =
                    snapshot.children.mapNotNull { it.getValue(AppTransaction::class.java) }
                        .filter { it.transactionBy == transactionFor }
                Result.success(appTransactions)
            }.catch {
                emit(Result.failure(it))
            }
}