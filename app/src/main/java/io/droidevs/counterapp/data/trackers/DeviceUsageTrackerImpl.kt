package io.droidevs.counterapp.data.trackers

import io.droidevs.counterapp.domain.trackers.DeviceUsageTracker

class DeviceUsageTrackerImpl : DeviceUsageTracker {
    override fun getScreenTime(): Int {
        // Mock implementation
        return 3600
    }
}
