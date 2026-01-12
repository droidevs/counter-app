package io.droidevs.counterapp.domain.trackers

interface NetworkConnectivityTracker {
    fun getMobileDataUsage(): Int
}
