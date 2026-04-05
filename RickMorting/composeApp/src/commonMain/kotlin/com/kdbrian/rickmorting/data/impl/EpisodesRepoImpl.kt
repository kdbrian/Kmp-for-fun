package com.kdbrian.rickmorting.data.impl

import com.apollographql.apollo.ApolloClient
import com.kdbrian.rickmorting.EpisodesQuery
import com.kdbrian.rickmorting.domain.repo.EpisodesRepo

class EpisodesRepoImpl(
    private val client : ApolloClient
) : EpisodesRepo {
    override suspend fun episodes(): Result<EpisodesQuery.Data> {
        val response = client.query(EpisodesQuery()).execute()
        return if (response.errors?.isNotEmpty() == true || response.data == null)
            Result.failure(
                Exception(response.errors?.joinToString { it.message })
            )
        else
            Result.success(response.data!!)
    }
}