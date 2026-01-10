package io.droidevs.counterapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.repository.DummyData
import javax.inject.Singleton
import io.droidevs.counterapp.repository.FakeCategoryRepository
import io.droidevs.counterapp.repository.FakeCounterRepository

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
    fun provideFakeCounterRepository(dummyData: DummyData): FakeCounterRepository {
        return FakeCounterRepository(dummyData)
    }

    @Provides
    @Singleton
    fun provideFakeCategoryRepository(dummyData: DummyData): FakeCategoryRepository {
        return FakeCategoryRepository(dummyData)
    }
}

