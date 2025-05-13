package com.sujoy.pbc.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitProvider {
    private var retrofit: Retrofit? = null

    fun init(baseUrl: String) {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)  // Increase connection timeout
            .readTimeout(30, TimeUnit.SECONDS)     // Increase read timeout
            .writeTimeout(30, TimeUnit.SECONDS)    // Increase write timeout
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient) // attach the custom client here
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getApi(): AttendanceApi {
        return retrofit?.create(AttendanceApi::class.java)
            ?: throw IllegalStateException("Retrofit not initialized!")
    }
}
