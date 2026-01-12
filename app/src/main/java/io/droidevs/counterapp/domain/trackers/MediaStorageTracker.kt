package io.droidevs.counterapp.domain.trackers

interface MediaStorageTracker {
    fun getPhotosCount(): Int
    fun getVideosCount(): Int
}
