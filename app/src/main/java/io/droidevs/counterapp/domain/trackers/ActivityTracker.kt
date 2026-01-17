package io.droidevs.counterapp.domain.trackers

@Deprecated(
    message = "Replaced by per-metric trackers with track(): Int (StepsTracker, DistanceTracker, FloorsTracker, ActiveMinutesTracker, CaloriesTracker).",
    level = DeprecationLevel.WARNING
)
interface ActivityTracker {
    fun getSteps(): Int
    fun getDistance(): Int
    fun getFloors(): Int
    fun getActiveMinutes(): Int
    fun getCalories(): Int
}
