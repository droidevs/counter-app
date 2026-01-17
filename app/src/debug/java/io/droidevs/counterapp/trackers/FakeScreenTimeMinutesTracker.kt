package io.droidevs.counterapp.trackers

import io.droidevs.counterapp.domain.trackers.ScreenTimeMinutesTracker

class FakeScreenTimeMinutesTracker : ScreenTimeMinutesTracker {
    override fun track(): Int = 7200
}

