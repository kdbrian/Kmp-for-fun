package com.kdbrian.rickmorting.di

import com.kdbrian.rickmorting.data.impl.CharactersRepoImpl
import com.kdbrian.rickmorting.data.impl.EpisodesRepoImpl
import com.kdbrian.rickmorting.data.impl.LocationsRepoImpl
import com.kdbrian.rickmorting.domain.repo.CharactersRepo
import com.kdbrian.rickmorting.domain.repo.EpisodesRepo
import com.kdbrian.rickmorting.domain.repo.LocationsRepo
import org.koin.dsl.module

fun dataModule() = module {
    single<LocationsRepo>{ LocationsRepoImpl(get()) }
    single<CharactersRepo>{ CharactersRepoImpl(get()) }
    single<EpisodesRepo>{ EpisodesRepoImpl(get()) }
}