package com.apska.data.di

import com.apska.data.repository.Repository
import com.apska.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindWeatherRepository(
        repository: Repository
    ) : WeatherRepository
}