package io.droidevs.counterapp.data.trackers

import android.content.Context
import io.droidevs.counterapp.domain.trackers.VideosTakenTracker

class VideosTakenTrackerImpl(private val context: Context) : VideosTakenTracker {
    override fun track(): Int {
        // TODO: Replace with MediaStore query.
        return 20
    }
}

