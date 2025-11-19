package com.apska.data.repository

import com.apska.data.dataSource.remote.WeatherApiService
import com.apska.domain.model.Weather
import com.apska.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class Repository @Inject constructor(
    private val weatherApiService : WeatherApiService
) : WeatherRepository {
    override fun getWeather(): Flow<Result<Weather>> = flow {
        val response = weatherApiService.getWeather()

        if (response.isSuccessful) {
            response.body()?.let {
                emit(Result.success(it))
            } ?: throw IllegalStateException("No weather data received")
        } else {
            emit(Result.failure(HttpException(response)))
        }
    }.catch { e ->
        emit(Result.failure(e))
    }
}