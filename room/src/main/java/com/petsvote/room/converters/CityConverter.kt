package com.petsvote.room.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.petsvote.room.City
import java.lang.reflect.Type
import java.util.*

class CityConverter {

    var gson = Gson()

    @TypeConverter
    fun foodItemToString(foodItems: List<City>): String {
        return gson.toJson(foodItems)
    }

    @TypeConverter
    fun stringToFoodItem(data: String): List<City> {
        val listType = object : TypeToken<List<City>>() {
        }.type
        return gson.fromJson(data, listType)
    }
}