package io.droidevs.counterapp.ui.hardware

import io.droidevs.counterapp.domain.result.errors.PreferenceError

/**
 * Handles errors that happen while reading the hardware-button-control preference.
 * Kept separate so we can swap implementations (analytics/logging) without touching MainActivity.
 */
fun interface HardwareButtonControlErrorHandler {
    fun onError(error: PreferenceError)
}

