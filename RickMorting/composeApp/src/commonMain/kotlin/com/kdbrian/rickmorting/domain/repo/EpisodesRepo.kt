package com.kdbrian.rickmorting.domain.repo

import com.kdbrian.rickmorting.EpisodesQuery

interface EpisodesRepo {
    suspend fun episodes():Result<EpisodesQuery.Data>
}