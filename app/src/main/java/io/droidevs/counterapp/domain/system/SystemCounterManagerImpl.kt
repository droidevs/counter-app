package io.droidevs.counterapp.domain.system

import io.droidevs.counterapp.domain.trackers.ActivityTracker
import io.droidevs.counterapp.domain.trackers.DeviceUsageTracker
import io.droidevs.counterapp.domain.trackers.MediaStorageTracker
import io.droidevs.counterapp.domain.trackers.NetworkConnectivityTracker

class SystemCounterManagerImpl(
    private val activityTracker: ActivityTracker,
    private val deviceUsageTracker: DeviceUsageTracker,
    private val mediaStorageTracker: MediaStorageTracker,
    private val networkConnectivityTracker: NetworkConnectivityTracker
) : SystemCounterManager {

    override fun fetchSystemCounters(): Map<SystemCounterType, Int> {
        return mapOf(
            SystemCounterType.STEPS to activityTracker.getSteps(),
            SystemCounterType.DISTANCE to activityTracker.getDistance(),
            SystemCounterType.FLOORS to activityTracker.getFloors(),
            SystemCounterType.ACTIVE_MINUTES to activityTracker.getActiveMinutes(),
            SystemCounterType.CALORIES to activityTracker.getCalories(),
            SystemCounterType.SCREEN_TIME to deviceUsageTracker.getScreenTime(),
            SystemCounterType.PHOTOS_TAKEN to mediaStorageTracker.getPhotosCount(),
            SystemCounterType.VIDEOS_TAKEN to mediaStorageTracker.getVideosCount(),
            SystemCounterType.MOBILE_DATA_USAGE to networkConnectivityTracker.getMobileDataUsage()
        )
    }
}
