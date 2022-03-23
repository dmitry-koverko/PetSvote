package com.petsvote.api

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.petsvote.api.adapter.NetworkResponseAdapterFactory
import com.petsvote.data.UserInfo
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession
import javax.net.ssl.HttpsURLConnection

class NetworkService() {

    private var mContext: Context? = null

    fun createService(context: Context): Api{
        mContext = context
        return create(Api::class.java, SettingsApi.BASE_URL)
    }

    fun createInstagramService(context: Context): ApiInstagram{
        mContext = context
        return create(ApiInstagram::class.java, SettingsApi.BASE_URL_INSTAGRAM)
    }

    private fun <T> create(apiClass: Class<T>, url: String): T {
        var token = mContext?.let { UserInfo.getBearer(it) } ?: ""
        val httpClient = if (token.isEmpty()) httpClientBuilder()
        else authHttpClient(token)
        return retrofitClient(httpClient, url).create(apiClass)
    }


    private fun retrofitClient(httpClient: OkHttpClient, url:String): Retrofit {

        val contentType = "application/json".toMediaType()

        return Retrofit.Builder().run {
            baseUrl(url)
            client(httpClient)
            addCallAdapterFactory(NetworkResponseAdapterFactory())
            addConverterFactory(Json {
                ignoreUnknownKeys = true
            }.asConverterFactory(contentType))
            build()
        }
    }

    private fun httpClientBuilder(): OkHttpClient{
        val client: OkHttpClient.Builder = OkHttpClient().newBuilder()
        client.apply {
            readTimeout(60L, TimeUnit.SECONDS)
            connectTimeout(60L, TimeUnit.SECONDS)
            writeTimeout(60L, TimeUnit.SECONDS)
        }
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        client.addInterceptor(interceptor).hostnameVerifier(object :HostnameVerifier {
            override fun verify(p0: String?, p1: SSLSession?): Boolean {
                val hv = HttpsURLConnection.getDefaultHostnameVerifier()
                return hv.verify("d.pvapi.site", p1)
            }

        })

        client.addInterceptor(interceptor).hostnameVerifier(object :HostnameVerifier {
            override fun verify(p0: String?, p1: SSLSession?): Boolean {
                val hv = HttpsURLConnection.getDefaultHostnameVerifier()
                return hv.verify("i.instagram.com", p1)
            }

        })

        mContext?.let {
            var bearer = UserInfo.getBearer(mContext!!)
            if(bearer.isNotEmpty()){
                client.addInterceptor { chain ->
                    chain.proceed(
                        chain.request().newBuilder().header("Authorization", "$bearer").build()
                    )
                }.build()
            }
        }

        client.addInterceptor { chain ->
            chain.proceed(
                chain.request().newBuilder().header("X-Api-Key", "r0ReOEO3749jg3rg").build()
            )
        }.build()
        return client.build()
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

        client.addInterceptor(interceptor).hostnameVerifier(object :HostnameVerifier {
            override fun verify(p0: String?, p1: SSLSession?): Boolean {
                val hv = HttpsURLConnection.getDefaultHostnameVerifier()
                return hv.verify("i.instagram.com", p1)
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
