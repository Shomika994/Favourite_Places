package com.example.favouriteplaces.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.favouriteplaces.models.FavouritePlaceModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [FavouritePlaceModel::class], version = 1, exportSchema = false)
abstract class FavouritePlacesDatabase : RoomDatabase() {

    abstract fun favouritePlaceDao(): FavouritePlaceDao

    companion object {
        @Volatile
        private var Instance: FavouritePlacesDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context): FavouritePlacesDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    FavouritePlacesDatabase::class.java,
                    "favouritePlace_database"
                ).build().also { Instance = it }
            }
        }
    }


}
