package io.droidevs.counterapp.trackers

import io.droidevs.counterapp.domain.trackers.MobileDataUsageTracker

class FakeMobileDataUsageTracker : MobileDataUsageTracker {
    override fun track(): Int = 1024
}

