package com.kdbrian.nursa.di

import com.kdbrian.nursa.core.network.ktorModule
import com.kdbrian.nursa.features.newsapp.di.newsModule
import org.koin.dsl.module

fun appModule() = module {


    includes(
        ktorModule(),
        newsModule()
    )

}