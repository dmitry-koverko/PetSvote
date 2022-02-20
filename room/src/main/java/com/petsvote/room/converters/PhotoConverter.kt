package com.petsvote.room.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.petsvote.room.City
import com.petsvote.room.Photo

class PhotoConverter {

    var gson = Gson()

    @TypeConverter
    fun photoItemToString(foodItems: List<Photo>): String {
        return gson.toJson(foodItems)
    }

    @TypeConverter
    fun stringToPhotoItem(data: String): List<Photo> {
        val listType = object : TypeToken<List<Photo>>() {
        }.type
        return gson.fromJson(data, listType)
    }
}