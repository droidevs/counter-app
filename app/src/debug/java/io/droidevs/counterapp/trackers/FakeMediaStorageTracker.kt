package io.droidevs.counterapp.trackers

import io.droidevs.counterapp.domain.trackers.MediaStorageTracker

class FakeMediaStorageTracker : MediaStorageTracker {
    override fun getPhotosCount(): Int {
        return 200
    }

    override fun getVideosCount(): Int {
        return 50
    }
}
