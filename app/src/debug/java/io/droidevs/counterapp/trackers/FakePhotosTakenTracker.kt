package io.droidevs.counterapp.trackers

import io.droidevs.counterapp.domain.trackers.PhotosTakenTracker

class FakePhotosTakenTracker : PhotosTakenTracker {
    override fun track(): Int = 200
}

