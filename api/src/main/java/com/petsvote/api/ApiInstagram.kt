package com.petsvote.api

import com.petsvote.api.adapter.NetworkResponse
import com.petsvote.api.entity.Breeds
import com.petsvote.api.entity.Error
import com.petsvote.api.entity.InstagramResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInstagram {
    @GET("{id}/info/")
    suspend fun getUsername(
        @Header("User-Agent") userAgent: String,
        @Path("id") id: Long): NetworkResponse<InstagramResponse, Error>
}
