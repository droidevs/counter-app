package io.droidevs.counterapp.di.repository

import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.BuildConfig
import io.droidevs.counterapp.data.dao.CategoryDao
import io.droidevs.counterapp.data.dao.CounterDao
import io.droidevs.counterapp.data.repository.CounterRepositoryImpl
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.repository.DummyData
import io.droidevs.counterapp.repository.FakeCounterRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CounterRepositoryModule {

    @Provides
    @Singleton
    fun provideCounterRepository(
        counterDao: Lazy<CounterDao>,
        categoryDao: Lazy<CategoryDao>,
        dummyData: Lazy<DummyData>
    ): CounterRepository {
        return if (BuildConfig.DEBUG) {
            FakeCounterRepository(dummyData.get())
        } else {
            CounterRepositoryImpl(counterDao.get(), categoryDao.get())
        }
    }
}
