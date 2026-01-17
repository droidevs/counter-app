package io.droidevs.counterapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.system.SystemCounterManager
import io.droidevs.counterapp.domain.system.SystemCounterManagerImpl
import io.droidevs.counterapp.domain.trackers.ActiveMinutesTracker
import io.droidevs.counterapp.domain.trackers.CaloriesTracker
import io.droidevs.counterapp.domain.trackers.DistanceTracker
import io.droidevs.counterapp.domain.trackers.FloorsTracker
import io.droidevs.counterapp.domain.trackers.MobileDataUsageTracker
import io.droidevs.counterapp.domain.trackers.PhotosTakenTracker
import io.droidevs.counterapp.domain.trackers.ScreenTimeMinutesTracker
import io.droidevs.counterapp.domain.trackers.StepsTracker
import io.droidevs.counterapp.domain.trackers.VideosTakenTracker
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SystemCounterModule {

    @Provides
    @Singleton
    fun provideSystemCounterManager(
        stepsTracker: StepsTracker,
        distanceTracker: DistanceTracker,
        floorsTracker: FloorsTracker,
        activeMinutesTracker: ActiveMinutesTracker,
        caloriesTracker: CaloriesTracker,
        screenTimeMinutesTracker: ScreenTimeMinutesTracker,
        photosTakenTracker: PhotosTakenTracker,
        videosTakenTracker: VideosTakenTracker,
        mobileDataUsageTracker: MobileDataUsageTracker,
        dispatchers: DispatcherProvider
    ): SystemCounterManager {
        return SystemCounterManagerImpl(
            stepsTracker = stepsTracker,
            distanceTracker = distanceTracker,
            floorsTracker = floorsTracker,
            activeMinutesTracker = activeMinutesTracker,
            caloriesTracker = caloriesTracker,
            screenTimeMinutesTracker = screenTimeMinutesTracker,
            photosTakenTracker = photosTakenTracker,
            videosTakenTracker = videosTakenTracker,
            mobileDataUsageTracker = mobileDataUsageTracker,
            dispatchers = dispatchers
        )
    }
}
