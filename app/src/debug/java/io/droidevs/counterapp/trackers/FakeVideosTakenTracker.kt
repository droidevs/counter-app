package io.droidevs.counterapp.trackers

import io.droidevs.counterapp.domain.trackers.VideosTakenTracker

class FakeVideosTakenTracker : VideosTakenTracker {
    override fun track(): Int = 50
}

