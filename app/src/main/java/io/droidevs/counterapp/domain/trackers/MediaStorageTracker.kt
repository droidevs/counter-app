package io.droidevs.counterapp.domain.trackers

interface MediaStorageTracker {
    fun getPhotosCount(): Int { return 0 }
    fun getVideosCount(): Int { return 0 }
    fun getFilesDownloaded(): Int { return 0 }
}