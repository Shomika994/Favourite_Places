package com.example.favouriteplaces.database

import com.example.favouriteplaces.models.FavouritePlaceModel
import kotlinx.coroutines.flow.Flow

class OfflineFavouritePlacesRepository(private val favouritePlaceDao: FavouritePlaceDao): FavouritePlacesRepository {

    override fun getAllFavouritePlacesStream(): Flow<List<FavouritePlaceModel>> {
        TODO("Not yet implemented")
    }

    override fun getFavouritePlacesStream(id: Int): Flow<FavouritePlaceModel?> {
        TODO("Not yet implemented")
    }

    override suspend fun insertFavouritePlace(item: FavouritePlaceModel) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFavouritePlace(item: FavouritePlaceModel) {
        TODO("Not yet implemented")
    }

    override suspend fun updateFavouritePlace(item: FavouritePlaceModel) {
        TODO("Not yet implemented")
    }


}