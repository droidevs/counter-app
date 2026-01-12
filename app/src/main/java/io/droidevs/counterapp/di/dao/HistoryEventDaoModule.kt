package io.droidevs.counterapp.di.dao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.data.AppDatabase
import io.droidevs.counterapp.data.dao.HistoryEventDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HistoryEventDaoModule {

    @Provides
    @Singleton
    fun provideHistoryEventDao(appDatabase: AppDatabase): HistoryEventDao {
        return appDatabase.historyEventDao()
    }
}
