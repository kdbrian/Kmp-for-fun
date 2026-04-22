package com.kdbrian.nursa.features.newsapp.di

import com.kdbrian.nursa.features.newsapp.NewsViewModel
import com.kdbrian.nursa.features.newsapp.data.impl.NewsRepoImpl
import com.kdbrian.nursa.features.newsapp.domain.repo.NewsRepo
import com.kdbrian.nursa.features.newsapp.domain.usecases.FetchTopNewsUseCase
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun newsModule() = module {

    single<NewsRepo> {
        NewsRepoImpl(client = get())
    }

    single { FetchTopNewsUseCase(newsRepo = get()) }
    viewModelOf(::NewsViewModel)
}