package io.droidevs.counterapp.data.trackers

import android.content.Context
import io.droidevs.counterapp.domain.trackers.PhotosTakenTracker

class PhotosTakenTrackerImpl(private val context: Context) : PhotosTakenTracker {
    override fun track(): Int {
        // TODO: Replace with MediaStore query.
        return 100
    }
}

