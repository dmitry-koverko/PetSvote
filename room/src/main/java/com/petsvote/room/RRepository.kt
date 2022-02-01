package com.petsvote.room

interface RRepository {

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

    suspend fun saveBreed(breed: Breed)
    suspend fun deleteBreeds()

    suspend fun saveCountries(info: CountryInfo)
    suspend fun getCounties(): List<Country>
}
