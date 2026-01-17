package io.droidevs.counterapp.data.trackers

import android.content.Context
import io.droidevs.counterapp.domain.trackers.MediaStorageTracker

@Deprecated("Replaced by PhotosTakenTracker/VideosTakenTracker; kept for compatibility.")
class MediaStorageTrackerImpl(private val context: Context) : MediaStorageTracker {
    private val photos = PhotosTakenTrackerImpl(context)
    private val videos = VideosTakenTrackerImpl(context)

    override fun getPhotosCount(): Int = photos.track()
    override fun getVideosCount(): Int = videos.track()
}
