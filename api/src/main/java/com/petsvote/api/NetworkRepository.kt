package com.petsvote.api

import android.content.Context
import android.util.Log
import com.petsvote.api.adapter.NetworkResponse
import com.petsvote.api.entity.*
import com.petsvote.data.UserInfo
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlin.Error

class NetworkRepository(private val context: Context): NReposirory {

    private val TAG = NetworkRepository::class.java.name
    private var api = NetworkService().createService(context)

    override suspend fun getTerms(loc: String): Document? {
        var result = api.getTerms(loc)
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

    override suspend fun getPolicy(loc: String): Document? {
        var result = api.getPolicy(loc)
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

    override suspend fun getBreeds(type: String?): List<Breeds>? {
        var types = mutableListOf<String>()
        type?.let { types.add(it)}
        var result = api.getBreeds(types, UserInfo.getLanguage(context))
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

    override suspend fun getRating(offset: Int?, id: Int?, limit: Int?, type: String?,
                                   sex: String?, city_id: Int?, countryId: Int?, ageBetween: String, breed_id: Int?): Rating? {
        var typFilter = when(UserInfo.getTabsFilter(context)){
            2 -> "global"
            1 -> "country"
            0 -> "city"
            else -> "global"
        }
        var lang = UserInfo.getLanguage(context)
        var result = api.getRating(limit, offset, lang, type, sex,
            city_id, countryId, ageBetween, typFilter, id, breed_id )
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

    override suspend fun getPetsList(offset: Int?): List<Pet>? {
        var result = api.getPets(null, offset, null, null, null, null,
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

    override suspend fun saveUserData(user: User, params: MultipartBody.Part?): UserData? {
        var loc =  user.location?.country?.let {
            Location(user.location?.city_id, user.location?.country_id,
                it, user.location.city)
        }
        var locString = Json.encodeToString(loc)
        Log.d(TAG, "location for save = $locString")
        var result = api.saveUserData(params, user.first_name, user.last_name, locString)
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

    override suspend fun addPet(
        photos:List<MultipartBody.Part>,
        bdate: String?,
        user_id: Int?,
        name: String?,
        breed_id: String?,
        sex: String?,
        type: String?
    ): Pet? {
        var result = api.addPet(photos, bdate, user_id, name, breed_id, sex, type)
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
        var result = api.getCountries(UserInfo.getLanguage(context), null)
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

    override suspend fun findPet(petId: Int): FindPet? {
        var lang = UserInfo.languge
        var result = api.findPet(lang, petId)
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

    override suspend fun petDetails(
        city_id: Int?,
        country_id: Int?,
        id: Int?,
        user_id: Int?
    ): PetDatails? {

        var result = api.getPetDetails(city_id, country_id, id, user_id)
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

    override suspend fun getCities(countryId: Int): List<City>? {
        var result = api.getCities("ru", null, countryId,null, 0)
        return when (result) {
            is NetworkResponse.Success -> {
                Log.d(TAG, result.toString())
                return result.body.cities
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