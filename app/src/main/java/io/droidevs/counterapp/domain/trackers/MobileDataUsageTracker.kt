package io.droidevs.counterapp.domain.trackers

interface MobileDataUsageTracker {
    fun track(): Int
}

