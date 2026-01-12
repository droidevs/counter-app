package io.droidevs.counterapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.data.trackers.*
import io.droidevs.counterapp.domain.trackers.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrackerModule {

    @Provides
    @Singleton
    fun provideActivityTracker(): ActivityTracker {
        return ActivityTrackerImpl()
    }

    @Provides
    @Singleton
    fun provideDeviceUsageTracker(): DeviceUsageTracker {
        return DeviceUsageTrackerImpl()
    }

    @Provides
    @Singleton
    fun provideMediaStorageTracker(@ApplicationContext context: Context): MediaStorageTracker {
        return MediaStorageTrackerImpl(context)
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivityTracker(@ApplicationContext context: Context): NetworkConnectivityTracker {
        return NetworkConnectivityTrackerImpl(context)
    }
}
