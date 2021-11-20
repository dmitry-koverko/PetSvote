package com.petsvote.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession
import javax.net.ssl.HttpsURLConnection




class NetworkService {

    fun createService(): Api{
        return create(Api::class.java)
    }

    private fun <T> create(apiClass: Class<T>): T {
        var token = "Bearer 83a72d7d24a858321170f6ac21a2659aea3b43d4"
        val httpClient = if (token.isEmpty()) httpClientBuilder().build()
        else authHttpClient(token)
        return retrofitClient(httpClient).create(apiClass)
    }


    private fun retrofitClient(httpClient: OkHttpClient): Retrofit {

        val contentType = "application/json".toMediaType()

        return Retrofit.Builder().run {
            baseUrl(SettingsApi.BASE_URL)
            client(httpClient)
            addConverterFactory(Json.asConverterFactory(contentType))
            build()
        }
    }

    private fun httpClientBuilder() = OkHttpClient.Builder().apply {
        readTimeout(60L, TimeUnit.SECONDS)
        connectTimeout(60L, TimeUnit.SECONDS)
        writeTimeout(60L, TimeUnit.SECONDS)
    }

    private fun authHttpClient(token: String): OkHttpClient {
        val client: OkHttpClient.Builder = OkHttpClient().newBuilder()

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        client.addInterceptor(interceptor).hostnameVerifier(object :HostnameVerifier {
            override fun verify(p0: String?, p1: SSLSession?): Boolean {
                val hv = HttpsURLConnection.getDefaultHostnameVerifier()
                return hv.verify("d.pvapi.site", p1)
            }

        })

        client.addInterceptor { chain ->
            chain.proceed(
                chain.request().newBuilder().header("Authorization", "$token").build()
            )
        }.build()

        client.addInterceptor { chain ->
            chain.proceed(
                chain.request().newBuilder().header("X-Api-Key", "r0ReOEO3749jg3rg").build()
            )
        }.build()
        return client.build()
    }



}
