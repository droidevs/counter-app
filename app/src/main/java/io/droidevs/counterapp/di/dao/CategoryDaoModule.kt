package io.droidevs.counterapp.di.dao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.data.AppDatabase
import io.droidevs.counterapp.data.dao.CategoryDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CategoryDaoModule {

    @Provides
    @Singleton
    fun provideCategoryDao(appDatabase: AppDatabase): CategoryDao {
        return appDatabase.categoryDao()
    }
}
