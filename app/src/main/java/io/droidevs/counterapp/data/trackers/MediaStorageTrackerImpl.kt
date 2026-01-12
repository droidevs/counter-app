package io.droidevs.counterapp.data.trackers

import android.content.Context
import io.droidevs.counterapp.domain.trackers.MediaStorageTracker

class MediaStorageTrackerImpl(private val context: Context) : MediaStorageTracker {
    override fun getPhotosCount(): Int {
        // Mock implementation
        return 100
    }

    override fun getVideosCount(): Int {
        // Mock implementation
        return 20
    }
}
