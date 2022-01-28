package com.petsvote.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class PSTypeConverters {
    companion object {

        var gson: Gson = Gson()

        @TypeConverter
        fun stringToUserPetsList(data: String?): List<UserPets> {
            if (data == null) {
                return Collections.emptyList()
            }
            val listType: Type = object : TypeToken<List<UserPets?>?>() {}.getType()
            return gson.fromJson(data, listType)
        }

        @TypeConverter
        fun userPetsListToString(someObjects: List<UserPets?>?): String {
            return gson.toJson(someObjects)
        }
    }
}