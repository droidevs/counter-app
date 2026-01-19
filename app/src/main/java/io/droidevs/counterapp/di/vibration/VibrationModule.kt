package io.droidevs.counterapp.di.vibration

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.data.vibration.AndroidCounterVibrator
import io.droidevs.counterapp.domain.vibration.CounterVibrator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class VibrationModule {

    @Binds
    @Singleton
    abstract fun bindCounterVibrator(impl: AndroidCounterVibrator): CounterVibrator
}

