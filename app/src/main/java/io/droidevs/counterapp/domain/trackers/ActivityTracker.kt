package io.droidevs.counterapp.domain.trackers

interface ActivityTracker {
    fun getSteps(): Int
    fun getDistance(): Int
    fun getFloors(): Int
    fun getActiveMinutes(): Int
    fun getCalories(): Int
}
