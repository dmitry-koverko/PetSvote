package com.petsvote.room.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.petsvote.room.Breed
import com.petsvote.room.City

class BreedsConverter {

    var gson = Gson()

    @TypeConverter
    fun breedItemToString(foodItems: List<Breed>): String {
        return gson.toJson(foodItems)
    }

    @TypeConverter
    fun stringToBreedItem(data: String): List<Breed> {
        val listType = object : TypeToken<List<Breed>>() {
        }.type
        return gson.fromJson(data, listType)
    }
}