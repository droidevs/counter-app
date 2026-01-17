package io.droidevs.counterapp.data.trackers

import io.droidevs.counterapp.domain.trackers.StepsTracker

class StepsTrackerImpl : StepsTracker {
    override fun track(): Int {
        // TODO: Replace with real implementation (Health Connect / sensors).
        return 1000
    }
}

