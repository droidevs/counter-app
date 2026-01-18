package io.droidevs.counterapp.ui.label

import io.droidevs.counterapp.domain.result.errors.PreferenceError

/**
 * Handles errors reading the "show labels" preference (category labels for counters).
 * Kept separate so we can add analytics later without affecting UI.
 */
fun interface LabelControlErrorHandler {
    fun onError(error: PreferenceError)
}

