package com.petsvote.room

import kotlinx.coroutines.flow.Flow

interface RRepository {

    suspend fun updateUser(userInfo: UserInfo)
    suspend fun saveRegister(bearer: String?,
                             id: Int?,
                             first_name: String?,
                             last_name: String?,
                             avatar: String?,
                             first_vote: Int?)

    suspend fun saveLocation(city_id: Int?,
                             country_id: Int?,
                             country: String?,
                             city: String?)
    suspend fun getLocation(): Location
    fun getCurrentUser(): Flow<UserInfo>

    suspend fun saveBreed(breed: Breed)
    suspend fun deleteBreeds()

    suspend fun saveCountries(info: CountryInfo)
    suspend fun getCounties(): List<Country>
}
