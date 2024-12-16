package com.example.projet_integration.network

import com.example.projet_integration.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // Base URL for your API
    private const val BASE_URL = "http://192.168.100.6:3001"

    // Logger to log network requests and responses
    private val logger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttpClient with logging interceptor
    private val client = OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()

    // Retrofit API service instance
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}
