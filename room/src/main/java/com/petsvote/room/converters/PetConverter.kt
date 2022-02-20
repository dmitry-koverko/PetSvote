package com.petsvote.room.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.petsvote.room.City
import com.petsvote.room.UserPets

class PetConverter {

    var gson = Gson()

    @TypeConverter
    fun petsItemToString(foodItems: List<UserPets>): String {
        return gson.toJson(foodItems)
    }

    @TypeConverter
    fun stringToPetsItem(data: String): List<UserPets> {
        val listType = object : TypeToken<List<UserPets>>() {
        }.type
        return gson.fromJson(data, listType)
    }
}