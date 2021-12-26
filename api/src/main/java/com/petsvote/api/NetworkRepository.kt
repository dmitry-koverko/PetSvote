package com.petsvote.api

import android.content.Context
import android.util.Log
import com.petsvote.api.adapter.NetworkResponse

class NetworkRepository(context: Context): NReposirory {

    private val TAG = NetworkRepository::class.java.name
    private var api = NetworkService().createService()

    override fun login() {

    }

    override suspend fun getConfig(): Boolean{
        val result = api.getGlobalConfig()
        return when (result) {
            is NetworkResponse.Success -> {
                Log.d(TAG, result.toString())
                return true
            }
            is NetworkResponse.ApiError -> {
                Log.d(TAG, result.toString())
                return false
            }
            is NetworkResponse.NetworkError -> {
                Log.d(TAG, result.toString())
                return false
            }
            is NetworkResponse.UnknownError -> {
                Log.d(TAG, result.toString())
                return false
            }
        }
    }

    override suspend fun getLocalize(): Boolean {
        val result = api.getLocalization(null)
        return when (result) {
            is NetworkResponse.Success -> {
                Log.d(TAG, result.toString())
                return true
            }
            is NetworkResponse.ApiError -> {
                Log.d(TAG, result.toString())
                return false
            }
            is NetworkResponse.NetworkError -> {
                Log.d(TAG, result.toString())
                return false
            }
            is NetworkResponse.UnknownError -> {
                Log.d(TAG, result.toString())
                return false
            }
        }
    }

}