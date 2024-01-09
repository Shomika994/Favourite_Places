package com.example.favouriteplaces.database

import android.content.Context

interface AppContainer {
    val favouritePlacesRepository: FavouritePlacesRepository
}

class AppDataContainer(private val context: Context) : AppContainer{

    override val favouritePlacesRepository: FavouritePlacesRepository by lazy {
        OfflineFavouritePlacesRepository(FavouritePlacesDatabase.getDatabase(context).favouritePlaceDao())
    }

}