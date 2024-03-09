package com.example.favouriteplaces.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favouritePlaces")
data class FavouritePlaceModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val title: String,
    val image: String,
    val description: String,
    val date: String,
    val savedAt: Long,
    val location: String,
    val latitude: Double,
    val longitude: Double
): Serializable