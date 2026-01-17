package io.droidevs.counterapp.trackers

import io.droidevs.counterapp.domain.trackers.DistanceTracker

class FakeDistanceTracker : DistanceTracker {
    override fun track(): Int = 4
}

