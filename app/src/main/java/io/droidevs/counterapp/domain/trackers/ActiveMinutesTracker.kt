package io.droidevs.counterapp.domain.trackers

interface ActiveMinutesTracker {
    fun track(): Int
}

