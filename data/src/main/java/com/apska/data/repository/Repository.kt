package com.apska.data.repository

import com.apska.data.dataSource.remote.WeatherApi
import com.apska.domain.model.Weather
import com.apska.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Repository @Inject constructor() : WeatherRepository {
    override fun getWeather(): Flow<Result<Weather>> = flow {
        try {
            val response = WeatherApi.api.getWeather()

            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Result.success(it))
                } ?: throw IllegalStateException("Response body is null")
            } else {
                val error = response.errorBody()?.string() ?: "Unknown error"
                throw IllegalStateException(error)
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}