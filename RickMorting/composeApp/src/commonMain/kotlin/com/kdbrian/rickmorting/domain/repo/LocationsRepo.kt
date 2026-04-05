package com.kdbrian.rickmorting.domain.repo

import com.kdbrian.rickmorting.LocationsQuery

interface LocationsRepo {
    suspend fun locations() : Result<LocationsQuery.Data>
}