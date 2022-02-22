package com.petsvote.api

import com.petsvote.api.adapter.NetworkResponse
import com.petsvote.api.entity.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.Query
import kotlin.Error

interface NReposirory {
    suspend fun register(code: String): Register?
    suspend fun getConfig(): GlobalConfig?
    suspend fun getLocalize():List<Localize>?
    suspend fun getBreeds(): List<Breeds>?
    suspend fun getRating(offset: Int?,  id: Int?): Rating?
    suspend fun getCurrentUser(): User?
    suspend fun getPetsList(): List<Pet>?
    suspend fun saveUserData(user: User, params: MultipartBody.Part?)
                : UserData?
    suspend fun addPet(photos:List<MultipartBody.Part>,
                       bdate: String?,
                       user_id: Int?,
                       name: String?,
                       breed_id: String?,
                       sex: String?,
                       type: String?): Pet?
    suspend fun getCountries(): List<Country>?
    suspend fun findPet(petId: Int): Pet?
    suspend fun petDetails(city_id: Int?, country_id: Int?, id: Int?, user_id: Int?): PetDatails?
    suspend fun getCities(countryId: Int): List<City>?
    abstract fun checkError(result: NetworkResponse<Any, Error>)
}
