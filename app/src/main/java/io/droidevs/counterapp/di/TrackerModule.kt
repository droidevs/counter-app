package io.droidevs.counterapp.di

import android.content.Context
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.BuildConfig
import io.droidevs.counterapp.data.trackers.ActiveMinutesTrackerImpl
import io.droidevs.counterapp.data.trackers.CaloriesTrackerImpl
import io.droidevs.counterapp.data.trackers.DistanceTrackerImpl
import io.droidevs.counterapp.data.trackers.FloorsTrackerImpl
import io.droidevs.counterapp.data.trackers.MobileDataUsageTrackerImpl
import io.droidevs.counterapp.data.trackers.PhotosTakenTrackerImpl
import io.droidevs.counterapp.data.trackers.ScreenTimeMinutesTrackerImpl
import io.droidevs.counterapp.data.trackers.StepsTrackerImpl
import io.droidevs.counterapp.data.trackers.VideosTakenTrackerImpl
import io.droidevs.counterapp.domain.trackers.ActiveMinutesTracker
import io.droidevs.counterapp.domain.trackers.CaloriesTracker
import io.droidevs.counterapp.domain.trackers.DistanceTracker
import io.droidevs.counterapp.domain.trackers.FloorsTracker
import io.droidevs.counterapp.domain.trackers.MobileDataUsageTracker
import io.droidevs.counterapp.domain.trackers.PhotosTakenTracker
import io.droidevs.counterapp.domain.trackers.ScreenTimeMinutesTracker
import io.droidevs.counterapp.domain.trackers.StepsTracker
import io.droidevs.counterapp.domain.trackers.VideosTakenTracker
import io.droidevs.counterapp.trackers.FakeActiveMinutesTracker
import io.droidevs.counterapp.trackers.FakeCaloriesTracker
import io.droidevs.counterapp.trackers.FakeDistanceTracker
import io.droidevs.counterapp.trackers.FakeFloorsTracker
import io.droidevs.counterapp.trackers.FakeMobileDataUsageTracker
import io.droidevs.counterapp.trackers.FakePhotosTakenTracker
import io.droidevs.counterapp.trackers.FakeScreenTimeMinutesTracker
import io.droidevs.counterapp.trackers.FakeStepsTracker
import io.droidevs.counterapp.trackers.FakeVideosTakenTracker
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrackerModule {

    @Provides
    @Singleton
    fun provideStepsTracker(): StepsTracker =
        if (BuildConfig.DEBUG) FakeStepsTracker() else StepsTrackerImpl()

    @Provides
    @Singleton
    fun provideDistanceTracker(): DistanceTracker =
        if (BuildConfig.DEBUG) FakeDistanceTracker() else DistanceTrackerImpl()

    @Provides
    @Singleton
    fun provideFloorsTracker(): FloorsTracker =
        if (BuildConfig.DEBUG) FakeFloorsTracker() else FloorsTrackerImpl()

    @Provides
    @Singleton
    fun provideActiveMinutesTracker(): ActiveMinutesTracker =
        if (BuildConfig.DEBUG) FakeActiveMinutesTracker() else ActiveMinutesTrackerImpl()

    @Provides
    @Singleton
    fun provideCaloriesTracker(): CaloriesTracker =
        if (BuildConfig.DEBUG) FakeCaloriesTracker() else CaloriesTrackerImpl()

    @Provides
    @Singleton
    fun provideScreenTimeMinutesTracker(): ScreenTimeMinutesTracker =
        if (BuildConfig.DEBUG) FakeScreenTimeMinutesTracker() else ScreenTimeMinutesTrackerImpl()

    @Provides
    @Singleton
    fun providePhotosTakenTracker(
        @ApplicationContext context: Lazy<Context>,
    ): PhotosTakenTracker =
        if (BuildConfig.DEBUG) FakePhotosTakenTracker() else PhotosTakenTrackerImpl(context.get())

    @Provides
    @Singleton
    fun provideVideosTakenTracker(
        @ApplicationContext context: Lazy<Context>,
    ): VideosTakenTracker =
        if (BuildConfig.DEBUG) FakeVideosTakenTracker() else VideosTakenTrackerImpl(context.get())

    @Provides
    @Singleton
    fun provideMobileDataUsageTracker(
        @ApplicationContext context: Lazy<Context>,
    ): MobileDataUsageTracker =
        if (BuildConfig.DEBUG) FakeMobileDataUsageTracker() else MobileDataUsageTrackerImpl(context.get())
}
