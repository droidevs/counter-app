package io.droidevs.counterapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.domain.repository.DataInitializer
import io.droidevs.counterapp.initializer.FakeDataInitializer
import io.droidevs.counterapp.repository.DummyData
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DebugInitializerModule {

    @Provides
    @Singleton
    fun provideDataInitializer(dummyData: DummyData): DataInitializer {
        return FakeDataInitializer(dummyData)
    }
}
