package io.droidevs.counterapp.data.trackers

import io.droidevs.counterapp.domain.trackers.ActivityTracker

@Deprecated("Replaced by per-metric trackers; kept for compatibility.")
class ActivityTrackerImpl : ActivityTracker {
    private val steps = StepsTrackerImpl()
    private val distance = DistanceTrackerImpl()
    private val floors = FloorsTrackerImpl()
    private val activeMinutes = ActiveMinutesTrackerImpl()
    private val calories = CaloriesTrackerImpl()

    override fun getSteps(): Int = steps.track()
    override fun getDistance(): Int = distance.track()
    override fun getFloors(): Int = floors.track()
    override fun getActiveMinutes(): Int = activeMinutes.track()
    override fun getCalories(): Int = calories.track()
}
