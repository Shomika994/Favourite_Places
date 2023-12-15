package com.example.favouriteplaces.database

import com.example.favouriteplaces.models.FavouritePlaceModel
import kotlinx.coroutines.flow.Flow

interface FavouritePlacesRepository {

    fun getAllFavouritePlacesStream(): Flow<List<FavouritePlaceModel>>

    fun getFavouritePlacesStream(id: Int): Flow<FavouritePlaceModel?>

    suspend fun insertFavouritePlace(item: FavouritePlaceModel)

    suspend fun deleteFavouritePlace(item: FavouritePlaceModel)

    suspend fun updateFavouritePlace(item: FavouritePlaceModel)
}