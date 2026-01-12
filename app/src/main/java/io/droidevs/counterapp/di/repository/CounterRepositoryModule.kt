package io.droidevs.counterapp.di.repository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.data.dao.CategoryDao
import io.droidevs.counterapp.data.dao.CounterDao
import io.droidevs.counterapp.data.repository.CounterRepositoryImpl
import io.droidevs.counterapp.domain.repository.CounterRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CounterRepositoryModule {

    @Provides
    @Singleton
    fun provideCounterRepository(counterDao: CounterDao, categoryDao: CategoryDao): CounterRepository {
        return CounterRepositoryImpl(counterDao, categoryDao)
    }
}
