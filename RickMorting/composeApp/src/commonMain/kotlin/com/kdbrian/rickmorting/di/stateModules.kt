package com.kdbrian.rickmorting.di

import com.kdbrian.rickmorting.presentation.viewmodel.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun stateModules() = module {
    viewModelOf(::MainViewModel)
}