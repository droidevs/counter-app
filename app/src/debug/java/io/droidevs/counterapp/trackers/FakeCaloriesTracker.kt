package io.droidevs.counterapp.trackers

import io.droidevs.counterapp.domain.trackers.CaloriesTracker

class FakeCaloriesTracker : CaloriesTracker {
    override fun track(): Int = 300
}

