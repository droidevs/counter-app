package io.droidevs.counterapp.di.repository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.data.dao.HistoryEventDao
import io.droidevs.counterapp.data.repository.HistoryRepositoryImpl
import io.droidevs.counterapp.domain.repository.HistoryRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HistoryRepositoryModule {

    @Provides
    @Singleton
    fun provideHistoryRepository(historyEventDao: HistoryEventDao): HistoryRepository {
        return HistoryRepositoryImpl(historyEventDao)
    }
}
