package io.droidevs.counterapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.util.TracingHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TracingModule {

    @Provides
    @Singleton
    fun provideTracingHelper(): TracingHelper = TracingHelper()
}
