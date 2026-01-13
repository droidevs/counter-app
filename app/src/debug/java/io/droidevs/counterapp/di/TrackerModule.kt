package io.droidevs.counterapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.domain.trackers.ActivityTracker
import io.droidevs.counterapp.domain.trackers.DeviceUsageTracker
import io.droidevs.counterapp.domain.trackers.MediaStorageTracker
import io.droidevs.counterapp.domain.trackers.NetworkConnectivityTracker
import io.droidevs.counterapp.trackers.FakeActivityTracker
import io.droidevs.counterapp.trackers.FakeDeviceUsageTracker
import io.droidevs.counterapp.trackers.FakeMediaStorageTracker
import io.droidevs.counterapp.trackers.FakeNetworkConnectivityTracker
import javax.inject.Singleton

// @Module
// @InstallIn(SingletonComponent::class)
@Deprecated("This module is deprecated and will be removed in a future version.")
object TrackerModule {

    // @Provides
    // @Singleton
    fun provideActivityTracker(): ActivityTracker {
        return FakeActivityTracker()
    }

    // @Provides
    // @Singleton
    fun provideDeviceUsageTracker(): DeviceUsageTracker {
        return FakeDeviceUsageTracker()
    }

    // @Provides
    // @Singleton
    fun provideMediaStorageTracker(): MediaStorageTracker {
        return FakeMediaStorageTracker()
    }

    // @Provides
    // @Singleton
    fun provideNetworkConnectivityTracker(): NetworkConnectivityTracker {
        return FakeNetworkConnectivityTracker()
    }
}
