package com.petsvote.api

import com.petsvote.api.adapter.NetworkResponse
import com.petsvote.api.entity.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import kotlin.Error

interface Api {

    @GET("v2/get-breed-list")
    suspend fun getBreeds(
        @Query("type[]") type: List<String>?,
        @Query("lang") lang: String?)
        : NetworkResponse<List<Breeds>, com.petsvote.api.entity.Error>

    @POST("register-apple")
    suspend fun register(@Query("code") code: String?):
            NetworkResponse<Register, com.petsvote.api.entity.Error>

    @GET("get-localization-data")
    suspend fun getLocalization(
        @Query("lang[]") lang: String?,
    ):  NetworkResponse<List<Localize>, com.petsvote.api.entity.Error>

    @GET("get-global-config")
    suspend fun getGlobalConfig(): NetworkResponse<GlobalConfig, com.petsvote.api.entity.Error>

    @GET("get-pets-list")
    suspend fun getPets(
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?,
        @Query("lang") lang: String?,
        @Query("type") type: String?,
        @Query("sex") sex: String?,
        @Query("city_id") city_id: Int?,
        @Query("country_id") country_id: Int?,
        @Query("age_between") age_between: String?,
        @Query("rating_type") rating_type: String?,
        @Query("ids") ids: String?,
    ): NetworkResponse<Pets, com.petsvote.api.entity.Error>

    @GET("get-rating-list")
    suspend fun getRating(
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?,
        @Query("lang") lang: String?,
        @Query("type") type: String?,
        @Query("sex") sex: String?,
        @Query("city_id") city_id: Int?,
        @Query("country_id") country_id: Int?,
        @Query("age_between") age_between: String?,
        @Query("rating_type") rating_type: String?,
        @Query("id") id: Int?,
        @Query("breed_id") breed_id: Int?,
    ): NetworkResponse<Rating, com.petsvote.api.entity.Error>


    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("add-vote")
    suspend fun addVote(
        @Field("from_user_id") from_user_id: Int?,
        @Field("to_pet_id") to_pet_id: Int?,
        @Field("mark") mark: Int?,
        @Field("first_vote") first_vote: Boolean?,
        @Field("name") name: String?,
        @Field("url_thumb") url_thumb: String?,
        @Field("id") id: Int?,
    )

    @GET("get-pet-details")
    suspend fun getPetDetails(
        @Query("type") type: String?,
        @Query("sex") sex: String?,
        @Query("city_id") city_id: Int?,
        @Query("country_id") country_id: Int?,
        @Query("age_between") age_between: String?,
        @Query("id") id: Int?,
        @Query("user_id") user_id: Int?,
        @Query("breed_id") breed_id: Int?,
    ): PetDatails

    @GET("get-current-user")
    suspend fun getCurrentUser(
        @Query("lang") lang: String?,
    ): NetworkResponse<User, com.petsvote.api.entity.Error>

    //@Multipart
    @POST("save-user-data")
    suspend fun saveUserData(
        //@PartMap params: Map<String, @JvmSuppressWildcards RequestBody>?,
        @Query("first_name") first_name: String?,
        @Query("last_name") last_name: String?,
        @Query("location") location: String?,
    ):  NetworkResponse<UserData, com.petsvote.api.entity.Error>


    @POST("add-complaint")
    suspend fun addComplaint(
        @Query("photo_id") photo_id: Int?,
        @Query("from_user_id") from_user_id: Int?,
    )

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("set-notify-status")
    suspend fun addVote(
        @Field("user_id") from_user_id: Int?,
        @Field("status") to_pet_id: Int?,
    )

    @Multipart
    @POST("save-user-data")
    suspend fun addPet(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Query("first_name") first_name: String?,
        @Query("bdate") bdate: String?,
        @Query("user_id") user_id: Int?,
        @Query("name") name: String?,
        @Query("breed_id") breed_id: String?,
        @Query("sex") sex: String?,
        @Query("type") type: String?,
    )

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("logout-user")
    suspend fun logout(
        @Field("user_id") user_id: Int?
    )

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("delete-user")
    suspend fun deleteUser(
        @Field("user_id") user_id: Int?
    )

    @GET("get-country-list")
    suspend fun getCountries(
        @Query("lang") lang: String?,
        @Query("country_name") country_name: String?,
    ): NetworkResponse<Countries, com.petsvote.api.entity.Error>


    @GET("get-city-list")
    suspend fun getCities(
        @Query("lang") lang: String?,
        @Query("city_name") city_name: String?,
        @Query("country_id") country_id: Int?,
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?,
    ): NetworkResponse<Cities, com.petsvote.api.entity.Error>

    @GET("get-city-country")
    suspend fun getCityCountry(
        @Query("lang") lang: String?,
        @Query("city_id") city_id: Int?,
    ): NetworkResponse<Countries, com.petsvote.api.entity.Error>

    @Multipart
    @POST("edit-pet")
    suspend fun editPet(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>)

    @POST("delete-pet")
    suspend fun deletePet(
        @Field("id") id: Int?)
}
