package com.petsvote.room.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.petsvote.room.City
import com.petsvote.room.Location

class LocationConverter {

    var gson = Gson()

    @TypeConverter
    fun locationToString(location: Location): String {
        return gson.toJson(location)
    }

    @TypeConverter
    fun stringToLocation(data: String): Location {
        val listType = object : TypeToken<Location>() {
        }.type
        return gson.fromJson(data, listType)
    }
}