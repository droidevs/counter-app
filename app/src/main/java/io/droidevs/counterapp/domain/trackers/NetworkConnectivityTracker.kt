package io.droidevs.counterapp.domain.trackers

@Deprecated(
    message = "Replaced by MobileDataUsageTracker with track(): Int.",
    level = DeprecationLevel.WARNING
)
interface NetworkConnectivityTracker {
    fun getMobileDataUsage(): Int
}
