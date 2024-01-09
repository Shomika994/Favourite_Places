package com.example.favouriteplaces.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.favouriteplaces.models.FavouritePlaceModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritePlaceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favouritePlaceModel: FavouritePlaceModel)

    @Update
    suspend fun update(favouritePlaceModel: FavouritePlaceModel)

    @Delete
    suspend fun delete(favouritePlaceModel: FavouritePlaceModel)

    @Query("SELECT * from favouritePlaces WHERE id = :id")
    fun getFavouritePlace(id: Int): Flow<FavouritePlaceModel>

    @Query("SELECT * from favouritePlaces ORDER BY date DESC")
    fun getAllData(): Flow<List<FavouritePlaceModel>>
}