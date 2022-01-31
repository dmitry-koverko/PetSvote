package com.petsvote.room.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.petsvote.room.City
import com.petsvote.room.Country

class CountryConverter {
    var gson = Gson()

    @TypeConverter
    fun foodItemToString(foodItems: List<Country>): String {
        return gson.toJson(foodItems)
    }

    @TypeConverter
    fun stringToFoodItem(data: String): List<Country> {
        val listType = object : TypeToken<List<Country>>() {
        }.type
        return gson.fromJson(data, listType)
    }
}