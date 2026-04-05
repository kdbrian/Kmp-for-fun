package com.kdbrian.rickmorting.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdbrian.rickmorting.AllCharactersQuery
import com.kdbrian.rickmorting.EpisodesQuery
import com.kdbrian.rickmorting.LocationsQuery
import com.kdbrian.rickmorting.domain.repo.CharactersRepo
import com.kdbrian.rickmorting.domain.repo.EpisodesRepo
import com.kdbrian.rickmorting.domain.repo.LocationsRepo
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val charactersRepo: CharactersRepo,
    private val locationsRepo: LocationsRepo,
    private val episodesRepo: EpisodesRepo
) : ViewModel() {


    private val _characters = MutableStateFlow<List<AllCharactersQuery.Result?>?>(null)
    val characters: StateFlow<List<AllCharactersQuery.Result?>?>
        get() = _characters.asStateFlow()


    private val _episodes = MutableStateFlow<List<EpisodesQuery.Result?>?>(null)
    val episodes: StateFlow<List<EpisodesQuery.Result?>?>
        get() = _episodes.asStateFlow()


    private val _locations = MutableStateFlow<List<LocationsQuery.Result?>?>(null)
    val locations: StateFlow<List<LocationsQuery.Result?>?>
        get() = _locations.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                charactersRepo
                    .characters()
                    .onSuccess { data ->
                        val allCharactersQueryResults: List<AllCharactersQuery.Result?>? =
                            data.characters?.results

                        Napier.d { "Got ${allCharactersQueryResults?.size}" }

                        _characters.emit(allCharactersQueryResults)
                    }
                    .onFailure {
                        Napier.d { "Failed ${it.message}" }
                    }
            }

            launch {
                locationsRepo
                    .locations()
                    .onSuccess { data ->
                        val locationsQueryResults = data.locations?.results
                        _locations.emit(locationsQueryResults)
                    }
            }

            launch {
                episodesRepo
                    .episodes()
                    .onSuccess { data ->
                        val episodesQueryResults = data.episodes?.results?.mapNotNull { it }
                        _episodes.emit(episodesQueryResults)
                    }
            }
        }
    }


}