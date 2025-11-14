package com.apska.data.dataSource.remote

import com.apska.data.json.WeatherJsonDeserializer
import com.apska.domain.model.Weather
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

object WeatherApi {
    private const val BASE_URL = "http://api.weatherapi.com/"
    private const val API_KEY = "fa8b3df74d4042b9aa7135114252304"

    val api: WeatherApiService by lazy {
        //val httpLoggingInterceptor = HttpLoggingInterceptor()
        //httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

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
            //.addInterceptor(httpLoggingInterceptor)
            .build()

        val gson = GsonBuilder()
            .registerTypeAdapter(Weather::class.java, WeatherJsonDeserializer())
            .create()

        val emptyConverterFactory = object : Converter.Factory() {
            fun converterFactory() = this
            override fun responseBodyConverter(
                type: Type,
                annotations: Array<out Annotation>,
                retrofit: Retrofit
            ) = object : Converter<ResponseBody, Any?> {
                val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)
                override fun convert(value: ResponseBody) =
                    if (value.contentLength() != 0L)
                        nextResponseBodyConverter.convert(value)
                    else
                        null
            }
        }

        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(emptyConverterFactory)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(WeatherApiService::class.java)
    }
}