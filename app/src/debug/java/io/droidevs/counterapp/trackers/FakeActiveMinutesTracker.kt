package io.droidevs.counterapp.trackers

import io.droidevs.counterapp.domain.trackers.ActiveMinutesTracker

class FakeActiveMinutesTracker : ActiveMinutesTracker {
    override fun track(): Int = 60
}

