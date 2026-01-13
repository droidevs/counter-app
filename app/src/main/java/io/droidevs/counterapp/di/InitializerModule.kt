package io.droidevs.counterapp.di

import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.BuildConfig
import io.droidevs.counterapp.data.dao.CategoryDao
import io.droidevs.counterapp.data.dao.CounterDao
import io.droidevs.counterapp.data.initializer.DefaultDataInitializerImpl
import io.droidevs.counterapp.domain.repository.DataInitializer
import io.droidevs.counterapp.initializer.FakeDataInitializer
import io.droidevs.counterapp.repository.DummyData
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InitializerModule {

    @Provides
    @Singleton
    fun provideDataInitializer(
        // These are the real dependencies from the 'main' source set
        categoryDao: Lazy<CategoryDao>,
        counterDao: Lazy<CounterDao>,
        // This is the fake dependency from the 'debug' source set
        dummyData: Lazy<DummyData>
    ): DataInitializer {
        return if (BuildConfig.DEBUG) {
            // This class is in 'debug' and will not be found when building 'release'
            FakeDataInitializer(dummyData.get())
        } else {
            DefaultDataInitializerImpl(categoryDao.get(), counterDao.get())
        }
    }
}
