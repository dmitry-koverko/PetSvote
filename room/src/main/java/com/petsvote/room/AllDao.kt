package com.petsvote.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM userinfo")
    fun getUser(): Flow<UserInfo>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userInfo: UserInfo)

    @Query("DELETE FROM UserInfo")
    suspend fun deleteAll()


//    @Query("UPDATE userinfo SET order_price=:price WHERE order_id = :id")
//    fun update(price: Float?, id: Int)

}

@Dao
interface LocationDao {

    @Query("SELECT * FROM location")
    fun getLocation(): Location

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(location: Location)

    @Query("DELETE FROM location")
    suspend fun deleteAll()

}

@Dao
interface BreedsDao{
    @Query("SELECT * FROM Breed")
    fun getBreeds(): List<Breed>

    @Query("SELECT * FROM Breed WHERE lang=:lang AND type=:type")
    fun getBreedsByKinds(lang: String, type: String?): List<Breed>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(breeds: List<Breed>)

    @Query("DELETE FROM BreedList")
    suspend fun deleteAll()
}

@Dao
interface CountryInfoDao{
    @Query("SELECT * FROM countryinfo")
    fun getCountryInfo(): CountryInfo

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(info: CountryInfo)

    @Query("DELETE FROM countryinfo")
    suspend fun deleteAll()
}


