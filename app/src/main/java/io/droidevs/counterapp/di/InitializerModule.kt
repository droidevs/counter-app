package io.droidevs.counterapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.data.dao.CategoryDao
import io.droidevs.counterapp.data.dao.CounterDao
import io.droidevs.counterapp.data.initializer.DefaultDataInitializerImpl
import io.droidevs.counterapp.domain.repository.DataInitializer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InitializerModule {

    @Provides
    @Singleton
    fun provideDataInitializer(
        categoryDao: CategoryDao,
        counterDao: CounterDao
    ): DataInitializer {
        return DefaultDataInitializerImpl(categoryDao, counterDao)
    }
}
