package com.kdbrian.rickmorting.data.impl

import com.apollographql.apollo.ApolloClient
import com.kdbrian.rickmorting.AllCharactersQuery
import com.kdbrian.rickmorting.domain.repo.CharactersRepo

class CharactersRepoImpl(
    private val client: ApolloClient
) : CharactersRepo {
    override suspend fun characters(): Result<AllCharactersQuery.Data> {
        val response = client.query(AllCharactersQuery()).execute()
        return if (response.errors?.isNotEmpty() == true || response.data == null)
            Result.failure(
                Exception(response.errors?.joinToString { it.message })
            )
        else
            Result.success(response.data!!)
    }
}