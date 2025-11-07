package com.apska.data.dataSource.remote

import com.apska.data.json.WeatherJsonDeserializer
import com.apska.domain.model.Weather
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherApi {
    private const val BASE_URL = "http://api.weatherapi.com/"
    private const val API_KEY = "fa8b3df74d4042b9aa7135114252304"

    val api: WeatherApiService by lazy {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val keyInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()

            val newUrl = originalRequest.url.newBuilder()
                .addQueryParameter("key", API_KEY)
                .build()

            val newRequest = originalRequest.newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(keyInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val gson = GsonBuilder()
            .registerTypeAdapter(Weather::class.java, WeatherJsonDeserializer())
            .create()

        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(WeatherApiService::class.java)
    }
}