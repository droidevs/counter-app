package io.droidevs.counterapp.data.trackers

import android.content.Context
import io.droidevs.counterapp.domain.trackers.NetworkConnectivityTracker

class NetworkConnectivityTrackerImpl(private val context: Context) : NetworkConnectivityTracker {
    override fun getMobileDataUsage(): Int {
        // Mock implementation
        return 500
    }
}
