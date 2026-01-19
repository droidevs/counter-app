package io.droidevs.counterapp.di.history

import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.data.AppDatabase
import io.droidevs.counterapp.data.history.RoomHistoryEventRecorder
import io.droidevs.counterapp.domain.history.HistoryEventRecorder
import io.droidevs.counterapp.domain.history.HistoryMergeConfig
import io.droidevs.counterapp.domain.repository.HistoryRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HistoryRecorderModule {

    @Provides
    @Singleton
    fun provideHistoryMergeConfig(): HistoryMergeConfig = HistoryMergeConfig()

    @Provides
    @Singleton
    fun provideHistoryEventRecorder(
        appDatabase: Lazy<AppDatabase>,
        repository: Lazy<HistoryRepository>,
        config: HistoryMergeConfig,
    ): HistoryEventRecorder {
        return RoomHistoryEventRecorder(appDatabase.get(), repository.get(), config)
    }
}
