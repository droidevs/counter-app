package io.droidevs.counterapp.data.trackers

import io.droidevs.counterapp.domain.trackers.ActivityTracker

class ActivityTrackerImpl : ActivityTracker {
    override fun getSteps(): Int {
        // Mock implementation
        return 1000
    }

    override fun getDistance(): Int {
        // Mock implementation
        return 2
    }

    override fun getFloors(): Int {
        // Mock implementation
        return 5
    }

    override fun getActiveMinutes(): Int {
        // Mock implementation
        return 30
    }

    override fun getCalories(): Int {
        // Mock implementation
        return 200
    }
}
