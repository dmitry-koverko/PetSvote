package com.petsvote.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.petsvote.room.converters.*


@Database(entities = arrayOf(UserInfo::class, UserPets::class, Location::class,
    BreedList::class, CountryInfo::class, Breed::class), version = 22, exportSchema = false)
@TypeConverters(CityConverter::class, CountryConverter::class, PetConverter::class,
    LocationConverter::class, PhotoConverter::class, BreedsConverter::class)
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