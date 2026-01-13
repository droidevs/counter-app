package io.droidevs.counterapp.trackers

import io.droidevs.counterapp.domain.trackers.ActivityTracker

class FakeActivityTracker : ActivityTracker {
    override fun getSteps(): Int {
        return 5000
    }

    override fun getDistance(): Int {
        return 4
    }

    override fun getFloors(): Int {
        return 10
    }

    override fun getActiveMinutes(): Int {
        return 60
    }

    override fun getCalories(): Int {
        return 300
    }
}
