package com.petsvote.api

import android.content.Context
import android.util.Log
import com.petsvote.api.adapter.NetworkResponse
import com.petsvote.api.entity.*
import com.petsvote.data.UserInfo
import okhttp3.RequestBody
import kotlin.Error

class NetworkRepository(private val context: Context): NReposirory {

    private val TAG = NetworkRepository::class.java.name
    private var api = NetworkService().createService(context)

    override suspend fun register(code: String): Register? {
        val result = api.register(code)
        return when(result){
            is NetworkResponse.Success -> {
                UserInfo.setBearer(context, result.body.bearer)
                Log.d(TAG, result.toString())
                return result.body
            }
            else -> {
                checkError(result as NetworkResponse<Any, Error>)
                return null
            }
        }
    }

    override suspend fun getConfig(): GlobalConfig?{
        val result = api.getGlobalConfig()
        return when (result) {
            is NetworkResponse.Success -> {
                Log.d(TAG, result.toString())
                return result.body
            }
            else -> {
                checkError(result as NetworkResponse<Any, Error>)
                return null
            }
        }
    }

    override suspend fun getLocalize(): List<Localize>? {
        val result = api.getLocalization(null)
        return when (result) {
            is NetworkResponse.Success -> {
                Log.d(TAG, result.toString())
                return result.body
            }
            else ->{
                checkError(result as NetworkResponse<Any, Error>)
                return null
            }
        }
    }

    override suspend fun getBreeds(): List<Breeds>? {
        var result = api.getBreeds(listOf("cat", "dog"), "ru")
        return when (result) {
            is NetworkResponse.Success -> {
                Log.d(TAG, result.toString())
                return result.body
            }
            else ->{
                checkError(result as NetworkResponse<Any, Error>)
                return null
            }
        }
    }

    override suspend fun getRating(offset: Int): Rating? {
        var result = api.getRating(null, offset, null, null, null,
            null, null, null, null, null, null )
        return when (result) {
            is NetworkResponse.Success -> {
                Log.d(TAG, result.toString())
                return result.body
            }
            else ->{
                checkError(result as NetworkResponse<Any, Error>)
                return null
            }
        }
    }

    override suspend fun getCurrentUser(): User? {
        var result = api.getCurrentUser("ru")
        return when (result) {
            is NetworkResponse.Success -> {
                Log.d(TAG, result.toString())
                return result.body
            }
            else ->{
                checkError(result as NetworkResponse<Any, Error>)
                return null
            }
        }
    }



    override suspend fun getPetsList(): List<Pet>? {
        var result = api.getPets(null, null, null, null, null, null,
                                null, null, null, null)
        return when (result) {
            is NetworkResponse.Success -> {
                Log.d(TAG, result.toString())
                return result.body.pets
            }
            else ->{
                checkError(result as NetworkResponse<Any, Error>)
                return null
            }
        }
    }

    override suspend fun saveUserData(user: User, params: Map<String, RequestBody>?): UserData? {
        var result = api.saveUserData(user.first_name, user.last_name, "")
        return when (result) {
            is NetworkResponse.Success -> {
                Log.d(TAG, result.toString())
                return result.body
            }
            else ->{
                checkError(result as NetworkResponse<Any, Error>)
                return null
            }
        }
    }

    override suspend fun getCountries(): List<Country>? {
        var result = api.getCountries("ru", null)
        return when (result) {
            is NetworkResponse.Success -> {
                Log.d(TAG, result.toString())
                return result.body.countries
            }
            else ->{
                checkError(result as NetworkResponse<Any, Error>)
                return null
            }
        }
    }

    override fun checkError(result: NetworkResponse<Any, Error>){
        when(result){
            is NetworkResponse.ApiError -> {
                Log.d(TAG, result.toString())
            }
            is NetworkResponse.NetworkError -> {
                Log.d(TAG, result.toString())
            }
            is NetworkResponse.UnknownError -> {
                Log.d(TAG, result.toString())
            }
        }
    }

}