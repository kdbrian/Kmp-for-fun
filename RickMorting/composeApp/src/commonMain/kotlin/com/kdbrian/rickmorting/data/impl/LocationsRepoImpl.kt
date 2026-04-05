package com.kdbrian.rickmorting.data.impl

import com.apollographql.apollo.ApolloClient
import com.kdbrian.rickmorting.LocationsQuery
import com.kdbrian.rickmorting.domain.repo.LocationsRepo

class LocationsRepoImpl(
    private val client: ApolloClient
) : LocationsRepo {

    override suspend fun locations(): Result<LocationsQuery.Data> {
        val response = client.query(LocationsQuery()).execute()
        return if (response.errors?.isNotEmpty() == true || response.data == null)
            Result.failure(
                Exception(response.errors?.joinToString { it.message })
            )
        else
            Result.success(response.data!!)
    }
}