package com.apska.power.di

import com.apska.domain.usecase.GetWeatherRemoteUseCase
import com.apska.domain.usecase.GetWeatherUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    abstract fun bindGetWeatherUseCase(
        getWeatherRemoteUseCase: GetWeatherRemoteUseCase
    ) : GetWeatherUseCase
}