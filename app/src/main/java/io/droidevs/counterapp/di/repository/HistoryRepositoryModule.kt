package io.droidevs.counterapp.di.repository

import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.BuildConfig
import io.droidevs.counterapp.data.dao.HistoryEventDao
import io.droidevs.counterapp.data.repository.HistoryRepositoryImpl
import io.droidevs.counterapp.domain.repository.HistoryRepository
import io.droidevs.counterapp.repository.DummyData
import io.droidevs.counterapp.repository.FakeHistoryRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HistoryRepositoryModule {

    @Provides
    @Singleton
    fun provideHistoryRepository(
        historyEventDao: Lazy<HistoryEventDao>,
        dummyData: Lazy<DummyData>
    ): HistoryRepository {
        return if (BuildConfig.DEBUG) {
            FakeHistoryRepository(dummyData.get())
        } else {
            HistoryRepositoryImpl(historyEventDao.get())
        }
    }
}
