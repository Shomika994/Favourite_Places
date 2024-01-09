package com.example.favouriteplaces.database

import com.example.favouriteplaces.models.FavouritePlaceModel
import kotlinx.coroutines.flow.Flow

class OfflineFavouritePlacesRepository(private val favouritePlaceDao: FavouritePlaceDao): FavouritePlacesRepository {

    override fun getAllFavouritePlacesStream(): Flow<List<FavouritePlaceModel>> {
       return favouritePlaceDao.getAllData()
    }

    override fun getFavouritePlacesStream(id: Int): Flow<FavouritePlaceModel?> {
        return favouritePlaceDao.getFavouritePlace(id)
    }

    override suspend fun insertFavouritePlace(item: FavouritePlaceModel) {
        favouritePlaceDao.insert(item)
    }

    override suspend fun deleteFavouritePlace(item: FavouritePlaceModel) {
        favouritePlaceDao.delete(item)
    }

    override suspend fun updateFavouritePlace(item: FavouritePlaceModel) {
        favouritePlaceDao.update(item)
    }

}