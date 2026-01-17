package io.droidevs.counterapp.data.trackers

import android.content.Context
import io.droidevs.counterapp.domain.trackers.MobileDataUsageTracker

class MobileDataUsageTrackerImpl(private val context: Context) : MobileDataUsageTracker {
    override fun track(): Int {
        // TODO: Replace with real NetworkStatsManager usage.
        return 500
    }
}

