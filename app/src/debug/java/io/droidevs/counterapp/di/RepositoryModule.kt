package io.droidevs.counterapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.repository.HistoryRepository
import io.droidevs.counterapp.repository.DummyData
import javax.inject.Singleton
import io.droidevs.counterapp.repository.FakeCategoryRepository
import io.droidevs.counterapp.repository.FakeCounterRepository
import io.droidevs.counterapp.repository.FakeHistoryRepository

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDummyData(): DummyData {
        return DummyData()
    }

    @Provides
    @Singleton
    fun provideFakeCounterRepository(dummyData: DummyData): CounterRepository {
        return FakeCounterRepository(dummyData)
    }

    @Provides
    @Singleton
    fun provideFakeCategoryRepository(dummyData: DummyData): CategoryRepository {
        return FakeCategoryRepository(dummyData)
    }

    @Provides
    @Singleton
    fun provideFakeHistoryRepository(dummyData: DummyData): HistoryRepository {
        return FakeHistoryRepository(dummyData)
    }
}

