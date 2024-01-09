package com.example.favouriteplaces

import android.annotation.SuppressLint
import android.content.Context
import com.example.favouriteplaces.database.AppDataContainer
import com.example.favouriteplaces.models.FavouritePlaceModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

object FavouritePlacesManager {

    private val _favouritePlaces = MutableStateFlow<List<FavouritePlaceModel>>(emptyList())
    val favouritePlaces: StateFlow<List<FavouritePlaceModel>> = _favouritePlaces

    @SuppressLint("StaticFieldLeak")
    var dataContainer: AppDataContainer? = null

    suspend fun fetchAllSavedPlaces(context: Context) {
        dataContainer = dataContainer ?: AppDataContainer(context)
        dataContainer?.favouritePlacesRepository?.getAllFavouritePlacesStream()
            ?.collect { fetched ->
                _favouritePlaces.update {
                    fetched.sortedWith(compareByDescending<FavouritePlaceModel> { it.savedAt }.thenByDescending { it.date })
                }

            }
    }

    suspend fun addFavouritePlace(
        favouritePlace: FavouritePlaceModel,
        applicationContext: Context,
    ) {
        dataContainer = dataContainer ?: AppDataContainer(applicationContext)
        dataContainer?.favouritePlacesRepository?.insertFavouritePlace(favouritePlace)
        _favouritePlaces.update { currentList ->
            currentList + favouritePlace
        }

    }

    fun getFavouritePlaces(): List<FavouritePlaceModel> {

        return favouritePlaces.value
    }
}
