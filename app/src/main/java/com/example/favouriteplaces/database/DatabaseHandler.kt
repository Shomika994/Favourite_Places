package com.example.favouriteplaces.database


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.favouriteplaces.models.FavouritePlaceModel


class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "HappyPlacesDatabase"
        private const val TABLE_FAVOURITE_PLACE = "HappyPlacesTable"

        private const val KEY_ID = "_id"
        private const val KEY_TITLE = "title"
        private const val KEY_IMAGE = "image"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_DATE = "date"
        private const val KEY_LOCATION = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val CREATE_FAVOURITE_PLACE_TABLE = ("CREATE TABLE " + TABLE_FAVOURITE_PLACE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT)")
        db?.execSQL(CREATE_FAVOURITE_PLACE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_FAVOURITE_PLACE")
        onCreate(db)
    }

    fun addFavouritePlace(favouritePlace: FavouritePlaceModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, favouritePlace.title)
        contentValues.put(KEY_IMAGE, favouritePlace.image)
        contentValues.put(
            KEY_DESCRIPTION,
            favouritePlace.description
        )
        contentValues.put(KEY_DATE, favouritePlace.date)
        contentValues.put(KEY_LOCATION, favouritePlace.location)
        contentValues.put(KEY_LATITUDE, favouritePlace.latitude)
        contentValues.put(KEY_LONGITUDE, favouritePlace.longitude)

        val result = db.insert(TABLE_FAVOURITE_PLACE, null, contentValues)

        db.close()
        return result
    }
}