package io.droidevs.counterapp.trackers

import io.droidevs.counterapp.domain.trackers.NetworkConnectivityTracker

@Deprecated("Replaced by MobileDataUsageTracker; kept for compatibility.")
class FakeNetworkConnectivityTracker : NetworkConnectivityTracker {
    private val mobile = FakeMobileDataUsageTracker()
    override fun getMobileDataUsage(): Int = mobile.track()
}
