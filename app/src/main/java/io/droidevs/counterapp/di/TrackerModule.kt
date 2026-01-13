package io.droidevs.counterapp.di

import android.content.Context
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.BuildConfig
import io.droidevs.counterapp.data.trackers.ActivityTrackerImpl
import io.droidevs.counterapp.data.trackers.DeviceUsageTrackerImpl
import io.droidevs.counterapp.data.trackers.MediaStorageTrackerImpl
import io.droidevs.counterapp.data.trackers.NetworkConnectivityTrackerImpl
import io.droidevs.counterapp.domain.trackers.ActivityTracker
import io.droidevs.counterapp.domain.trackers.DeviceUsageTracker
import io.droidevs.counterapp.domain.trackers.MediaStorageTracker
import io.droidevs.counterapp.domain.trackers.NetworkConnectivityTracker
import io.droidevs.counterapp.trackers.FakeActivityTracker
import io.droidevs.counterapp.trackers.FakeDeviceUsageTracker
import io.droidevs.counterapp.trackers.FakeMediaStorageTracker
import io.droidevs.counterapp.trackers.FakeNetworkConnectivityTracker
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrackerModule {

    @Provides
    @Singleton
    fun provideActivityTracker(): ActivityTracker {
        return if (BuildConfig.DEBUG) {
            FakeActivityTracker()
        } else {
            ActivityTrackerImpl()
        }
    }

    @Provides
    @Singleton
    fun provideDeviceUsageTracker(): DeviceUsageTracker {
        return if (BuildConfig.DEBUG) {
            FakeDeviceUsageTracker()
        } else {
            DeviceUsageTrackerImpl()
        }
    }

    @Provides
    @Singleton
    fun provideMediaStorageTracker(
        @ApplicationContext context: Lazy<Context>,
    ): MediaStorageTracker {
        return if (BuildConfig.DEBUG) {
            FakeMediaStorageTracker()
        } else {
            MediaStorageTrackerImpl(context.get())
        }
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivityTracker(
        @ApplicationContext context: Lazy<Context>,
    ): NetworkConnectivityTracker {
        return if (BuildConfig.DEBUG) {
            FakeNetworkConnectivityTracker()
        } else {
            NetworkConnectivityTrackerImpl(context.get())
        }
    }
}
