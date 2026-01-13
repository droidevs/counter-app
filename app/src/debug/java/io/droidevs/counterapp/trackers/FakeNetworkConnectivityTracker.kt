package io.droidevs.counterapp.trackers

import io.droidevs.counterapp.domain.trackers.NetworkConnectivityTracker

class FakeNetworkConnectivityTracker : NetworkConnectivityTracker {
    override fun getMobileDataUsage(): Int {
        return 1024
    }
}
