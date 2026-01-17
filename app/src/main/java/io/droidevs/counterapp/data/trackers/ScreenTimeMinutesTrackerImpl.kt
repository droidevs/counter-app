package io.droidevs.counterapp.data.trackers

import io.droidevs.counterapp.domain.trackers.ScreenTimeMinutesTracker

class ScreenTimeMinutesTrackerImpl : ScreenTimeMinutesTracker {
    override fun track(): Int {
        // TODO: Replace with real implementation.
        // Previously DeviceUsageTrackerImpl returned 3600; keep similar placeholder.
        return 3600
    }
}

