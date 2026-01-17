package io.droidevs.counterapp.data.trackers

import io.droidevs.counterapp.domain.trackers.DeviceUsageTracker

@Deprecated("Replaced by ScreenTimeMinutesTracker; kept for compatibility.")
class DeviceUsageTrackerImpl : DeviceUsageTracker {
    private val screenTime = ScreenTimeMinutesTrackerImpl()
    override fun getScreenTime(): Int = screenTime.track()
}
