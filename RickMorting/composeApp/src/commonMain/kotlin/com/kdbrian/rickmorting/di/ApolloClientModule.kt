@file:OptIn(ApolloExperimental::class)

package com.kdbrian.rickmorting.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.annotations.ApolloExperimental
import com.apollographql.apollo.api.ApolloRequest
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.interceptor.ApolloInterceptor
import com.apollographql.apollo.interceptor.ApolloInterceptorChain
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import org.koin.dsl.module

private const val TAG = "ApolloClientModule"
fun apolloClientModule() = module {
    single {
        ApolloClient.Builder()
            .serverUrl("https://rickandmortyapi.com/graphql")
//            .retryOnError { true }
            .build()
    }
}