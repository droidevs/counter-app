package io.droidevs.counterapp.domain.trackers

@Deprecated(
    message = "Replaced by ScreenTimeMinutesTracker with track(): Int.",
    level = DeprecationLevel.WARNING
)
interface DeviceUsageTracker {
    fun getScreenTime(): Int
}
