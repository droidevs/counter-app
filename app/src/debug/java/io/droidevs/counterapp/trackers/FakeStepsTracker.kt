package io.droidevs.counterapp.trackers

import io.droidevs.counterapp.domain.trackers.StepsTracker

class FakeStepsTracker : StepsTracker {
    override fun track(): Int = 5000
}

