package io.droidevs.counterapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.domain.system.SystemCounterManager
import io.droidevs.counterapp.domain.system.SystemCounterManagerImpl
import io.droidevs.counterapp.domain.trackers.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SystemCounterModule {

    @Provides
    @Singleton
    fun provideSystemCounterManager(
        activityTracker: ActivityTracker,
        deviceUsageTracker: DeviceUsageTracker,
        mediaStorageTracker: MediaStorageTracker,
        networkConnectivityTracker: NetworkConnectivityTracker
    ): SystemCounterManager {
        return SystemCounterManagerImpl(
            activityTracker,
            deviceUsageTracker,
            mediaStorageTracker,
            networkConnectivityTracker
        )
    }
}
