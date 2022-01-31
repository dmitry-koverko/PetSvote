package com.petsvote.api

import com.petsvote.api.adapter.NetworkResponse
import com.petsvote.api.entity.*
import okhttp3.RequestBody
import kotlin.Error

interface NReposirory {
    suspend fun register(code: String): Register?
    suspend fun getConfig(): GlobalConfig?
    suspend fun getLocalize():List<Localize>?
    suspend fun getBreeds(): List<Breeds>?
    suspend fun getRating(offset: Int): Rating?
    suspend fun getCurrentUser(): User?
    suspend fun getPetsList(): List<Pet>?
    suspend fun saveUserData(user: User, params: Map<String, @JvmSuppressWildcards RequestBody>?)
                : UserData?
    suspend fun getCountries(): List<Country>?
    abstract fun checkError(result: NetworkResponse<Any, Error>)
}
