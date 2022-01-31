package com.petsvote.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.petsvote.room.converters.CityConverter
import com.petsvote.room.converters.CountryConverter


@Database(entities = arrayOf(UserInfo::class, UserPets::class, Location::class,
          Breed::class, CountryInfo::class), version = 3, exportSchema = false)
@TypeConverters(CityConverter::class, CountryConverter::class)
public abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun locationDao(): LocationDao
    abstract fun breedDao(): BreedsDao
    abstract fun countryInfoDao(): CountryInfoDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}