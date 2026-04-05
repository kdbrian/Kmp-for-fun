package com.kdbrian.rickmorting.di

import org.koin.dsl.module

fun appModules() = module {
    includes(
        dataModule(),
        apolloClientModule(),
        stateModules()
    )
}