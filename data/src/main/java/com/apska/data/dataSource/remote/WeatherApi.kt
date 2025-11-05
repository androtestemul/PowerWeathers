package com.apska.data.dataSource.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherApi {
    private const val BASE_URL = "http://api.weatherapi.com/"
    private const val AUTH_TOKEN = "fa8b3df74d4042b9aa7135114252304"

    val api: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }
}