package io.droidevs.counterapp.domain.trackers

interface ActivityTracker {
    fun getSteps(): Int { return 0 }
    fun getDistance(): Int { return 0 }
    fun getFloors(): Int { return 0 }
    fun getActiveMinutes(): Int { return 0 }

    fun getCalories(): Int { return 0 }
}