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

    suspend fun deleteFavouritePlace(favouritePlaceId: Int) {

        val placeToDelete = favouritePlaces.value.find { it.id == favouritePlaceId }

        placeToDelete?.let {
            dataContainer?.favouritePlacesRepository?.deleteFavouritePlace(it)
            _favouritePlaces.update { currentList ->
                currentList.filterNot { place -> place.id == placeToDelete.id }
            }
        }
    }

    suspend fun updateFavouritePlace(
        placeToEdit: FavouritePlaceModel,
        context: Context
    ) {
        dataContainer = dataContainer ?: AppDataContainer(context)
        dataContainer?.favouritePlacesRepository?.updateFavouritePlace(placeToEdit)
        _favouritePlaces.update { currentList ->
            currentList + placeToEdit
        }

    }


    fun getFavouritePlaces(): List<FavouritePlaceModel> {
        return favouritePlaces.value
    }
}
