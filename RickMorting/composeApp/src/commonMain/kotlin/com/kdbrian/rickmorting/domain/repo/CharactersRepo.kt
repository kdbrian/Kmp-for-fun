package com.kdbrian.rickmorting.domain.repo

import com.kdbrian.rickmorting.AllCharactersQuery

interface CharactersRepo {
    suspend fun characters() : Result<AllCharactersQuery.Data>
}