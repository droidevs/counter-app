package io.droidevs.counterapp.trackers

import io.droidevs.counterapp.domain.trackers.ActivityTracker

@Deprecated("Replaced by per-metric trackers; kept for compatibility.")
class FakeActivityTracker : ActivityTracker {
    private val steps = FakeStepsTracker()
    private val distance = FakeDistanceTracker()
    private val floors = FakeFloorsTracker()
    private val activeMinutes = FakeActiveMinutesTracker()
    private val calories = FakeCaloriesTracker()

    override fun getSteps(): Int = steps.track()
    override fun getDistance(): Int = distance.track()
    override fun getFloors(): Int = floors.track()
    override fun getActiveMinutes(): Int = activeMinutes.track()
    override fun getCalories(): Int = calories.track()
}
