package io.droidevs.counterapp.di.repository

import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.BuildConfig
import io.droidevs.counterapp.data.dao.CategoryDao
import io.droidevs.counterapp.data.repository.CategoryRepositoryImpl
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.repository.DummyData
import io.droidevs.counterapp.repository.FakeCategoryRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CategoryRepositoryModule {

    @Provides
    @Singleton
    fun provideCategoryRepository(
        categoryDao: Lazy<CategoryDao>,
        dummyData: Lazy<DummyData>
    ): CategoryRepository {
        return if (BuildConfig.DEBUG) {
            FakeCategoryRepository(dummyData.get())
        } else {
            CategoryRepositoryImpl(categoryDao.get())
        }
    }
}
