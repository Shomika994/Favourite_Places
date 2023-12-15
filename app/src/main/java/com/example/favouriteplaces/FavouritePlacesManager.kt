package com.example.favouriteplaces

import com.example.favouriteplaces.models.FavouritePlaceModel

object FavouritePlacesManager {

    private val favouritePlaces = mutableListOf<FavouritePlaceModel>()

    fun addFavouritePlace(favouritePlace: FavouritePlaceModel) {
        favouritePlaces.add(favouritePlace)
    }

    fun getFavouritePlaces(): List<FavouritePlaceModel> {
        return favouritePlaces.toList()
    }
}
