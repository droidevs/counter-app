package io.droidevs.counterapp.di.repository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.data.dao.CategoryDao
import io.droidevs.counterapp.data.repository.CategoryRepositoryImpl
import io.droidevs.counterapp.domain.repository.CategoryRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CategoryRepositoryModule {

    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
        return CategoryRepositoryImpl(categoryDao)
    }
}
