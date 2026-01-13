package io.droidevs.counterapp.trackers

import io.droidevs.counterapp.domain.trackers.DeviceUsageTracker

class FakeDeviceUsageTracker : DeviceUsageTracker {
    override fun getScreenTime(): Int {
        return 7200
    }
}
