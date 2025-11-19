package com.apska.data.di

import com.apska.data.dataSource.remote.WeatherApi
import com.apska.data.dataSource.remote.WeatherApiService
import com.apska.data.repository.Repository
import com.apska.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideWeatherApiService() : WeatherApiService {
        return WeatherApi.api
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(weatherApiService: WeatherApiService) : WeatherRepository {
        return Repository(weatherApiService)
    }
}