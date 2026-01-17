package io.droidevs.counterapp.trackers

import io.droidevs.counterapp.domain.trackers.DeviceUsageTracker

@Deprecated("Replaced by ScreenTimeMinutesTracker; kept for compatibility.")
class FakeDeviceUsageTracker : DeviceUsageTracker {
    private val screenTime = FakeScreenTimeMinutesTracker()
    override fun getScreenTime(): Int = screenTime.track()
}
