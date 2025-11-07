package com.apska.data.dataSource.remote

import com.apska.domain.model.Weather
import retrofit2.Response
import retrofit2.http.GET

interface WeatherApiService {
    @GET("v1/forecast.json?q=55.7569,37.6151&days=3")
    suspend fun getWeather() : Response<Weather>
}