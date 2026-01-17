package io.droidevs.counterapp.trackers

import io.droidevs.counterapp.domain.trackers.MediaStorageTracker

@Deprecated("Replaced by PhotosTakenTracker/VideosTakenTracker; kept for compatibility.")
class FakeMediaStorageTracker : MediaStorageTracker {
    private val photos = FakePhotosTakenTracker()
    private val videos = FakeVideosTakenTracker()

    override fun getPhotosCount(): Int = photos.track()
    override fun getVideosCount(): Int = videos.track()
}
