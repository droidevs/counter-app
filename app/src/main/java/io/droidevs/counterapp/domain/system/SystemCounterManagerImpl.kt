package io.droidevs.counterapp.domain.system

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.trackers.ActiveMinutesTracker
import io.droidevs.counterapp.domain.trackers.CaloriesTracker
import io.droidevs.counterapp.domain.trackers.DistanceTracker
import io.droidevs.counterapp.domain.trackers.FloorsTracker
import io.droidevs.counterapp.domain.trackers.MobileDataUsageTracker
import io.droidevs.counterapp.domain.trackers.PhotosTakenTracker
import io.droidevs.counterapp.domain.trackers.ScreenTimeMinutesTracker
import io.droidevs.counterapp.domain.trackers.StepsTracker
import io.droidevs.counterapp.domain.trackers.VideosTakenTracker
import kotlinx.coroutines.withContext

class SystemCounterManagerImpl(
    private val stepsTracker: StepsTracker,
    private val distanceTracker: DistanceTracker,
    private val floorsTracker: FloorsTracker,
    private val activeMinutesTracker: ActiveMinutesTracker,
    private val caloriesTracker: CaloriesTracker,
    private val screenTimeMinutesTracker: ScreenTimeMinutesTracker,
    private val photosTakenTracker: PhotosTakenTracker,
    private val videosTakenTracker: VideosTakenTracker,
    private val mobileDataUsageTracker: MobileDataUsageTracker,
    private val dispatchers: DispatcherProvider
) : SystemCounterManager {

    override suspend fun fetchSystemCounters(): Map<SystemCounterType, Int> = withContext(dispatchers.default) {
        // Each value is isolated so one tracker failure doesn't prevent others from syncing.
        mapOf(
            SystemCounterType.STEPS to runCatching { stepsTracker.track() }.getOrDefault(0),
            SystemCounterType.DISTANCE to runCatching { distanceTracker.track() }.getOrDefault(0),
            SystemCounterType.FLOORS to runCatching { floorsTracker.track() }.getOrDefault(0),
            SystemCounterType.ACTIVE_MINUTES to runCatching { activeMinutesTracker.track() }.getOrDefault(0),
            SystemCounterType.CALORIES to runCatching { caloriesTracker.track() }.getOrDefault(0),
            SystemCounterType.SCREEN_TIME to runCatching { screenTimeMinutesTracker.track() }.getOrDefault(0),
            SystemCounterType.PHOTOS_TAKEN to runCatching { photosTakenTracker.track() }.getOrDefault(0),
            SystemCounterType.VIDEOS_TAKEN to runCatching { videosTakenTracker.track() }.getOrDefault(0),
            SystemCounterType.MOBILE_DATA_USAGE to runCatching { mobileDataUsageTracker.track() }.getOrDefault(0)
        )
    }
}
