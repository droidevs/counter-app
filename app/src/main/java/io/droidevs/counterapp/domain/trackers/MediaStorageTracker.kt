package io.droidevs.counterapp.domain.trackers

@Deprecated(
    message = "Replaced by PhotosTakenTracker/VideosTakenTracker with track(): Int.",
    level = DeprecationLevel.WARNING
)
interface MediaStorageTracker {
    fun getPhotosCount(): Int
    fun getVideosCount(): Int
}
