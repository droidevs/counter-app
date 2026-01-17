package io.droidevs.counterapp.trackers

import io.droidevs.counterapp.domain.trackers.FloorsTracker

class FakeFloorsTracker : FloorsTracker {
    override fun track(): Int = 10
}

