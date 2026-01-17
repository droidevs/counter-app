package io.droidevs.counterapp.data.trackers

import android.content.Context
import io.droidevs.counterapp.domain.trackers.NetworkConnectivityTracker

@Deprecated("Replaced by MobileDataUsageTracker; kept for compatibility.")
class NetworkConnectivityTrackerImpl(private val context: Context) : NetworkConnectivityTracker {
    private val mobile = MobileDataUsageTrackerImpl(context)
    override fun getMobileDataUsage(): Int = mobile.track()
}
