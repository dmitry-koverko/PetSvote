package com.petsvote.api

import com.petsvote.api.adapter.NetworkResponse
import com.petsvote.api.entity.*
import kotlin.Error

interface NReposirory {
    suspend fun register(code: String): Register?
    suspend fun getConfig(): GlobalConfig?
    suspend fun getLocalize():List<Localize>?
    suspend fun getBreeds(): List<Breeds>?
    suspend fun getRating(offset: Int): Rating?
    suspend fun getCurrentUser(): User?
    suspend fun getPetsList(): List<Pet>?
    abstract fun checkError(result: NetworkResponse<Any, Error>)
}
