package com.apska.data.dataSource.remote

import com.apska.domain.model.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("v1/forecast.json")
    suspend fun getWeather(
        @Query("q") location: String = "55.7569,37.6151",
        @Query("days") days: Int = 3,
        @Query("lang") lang: String = "ru"
    ) : Response<Weather>
}