package io.droidevs.counterapp.di.dao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.data.AppDatabase
import io.droidevs.counterapp.data.dao.CounterDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideCounterDao(appDatabase: AppDatabase): CounterDao {
        return appDatabase.counterDao()
    }
}
