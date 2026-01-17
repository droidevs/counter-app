package io.droidevs.counterapp.domain.system

import io.droidevs.counterapp.domain.trackers.ActivityTracker
import io.droidevs.counterapp.domain.trackers.DeviceUsageTracker
import io.droidevs.counterapp.domain.trackers.MediaStorageTracker
import io.droidevs.counterapp.domain.trackers.NetworkConnectivityTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SystemCounterManagerImpl(
    private val activityTracker: ActivityTracker,
    private val deviceUsageTracker: DeviceUsageTracker,
    private val mediaStorageTracker: MediaStorageTracker,
    private val networkConnectivityTracker: NetworkConnectivityTracker
) : SystemCounterManager {

    override suspend fun fetchSystemCounters(): Map<SystemCounterType, Int> = withContext(Dispatchers.Default) {
        // Each value is isolated so one tracker failure doesn't prevent others from syncing.
        mapOf(
            SystemCounterType.STEPS to runCatching { activityTracker.getSteps() }.getOrDefault(0),
            SystemCounterType.DISTANCE to runCatching { activityTracker.getDistance() }.getOrDefault(0),
            SystemCounterType.FLOORS to runCatching { activityTracker.getFloors() }.getOrDefault(0),
            SystemCounterType.ACTIVE_MINUTES to runCatching { activityTracker.getActiveMinutes() }.getOrDefault(0),
            SystemCounterType.CALORIES to runCatching { activityTracker.getCalories() }.getOrDefault(0),
            SystemCounterType.SCREEN_TIME to runCatching { deviceUsageTracker.getScreenTime() }.getOrDefault(0),
            SystemCounterType.PHOTOS_TAKEN to runCatching { mediaStorageTracker.getPhotosCount() }.getOrDefault(0),
            SystemCounterType.VIDEOS_TAKEN to runCatching { mediaStorageTracker.getVideosCount() }.getOrDefault(0),
            SystemCounterType.MOBILE_DATA_USAGE to runCatching { networkConnectivityTracker.getMobileDataUsage() }.getOrDefault(0)
        )
    }
}
