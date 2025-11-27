package com.apska.power.di

import com.apska.domain.repository.WeatherRepository
import com.apska.domain.usecase.GetWeatherRemoteUseCase
import com.apska.domain.usecase.GetWeatherUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    fun provideGetWeatherUseCase(
        weatherRepository: WeatherRepository
    ) : GetWeatherUseCase = GetWeatherRemoteUseCase(weatherRepository)
}