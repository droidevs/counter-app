package io.droidevs.counterapp.ui.feedback

import io.droidevs.counterapp.domain.sound.SoundError
import io.droidevs.counterapp.domain.vibration.VibrationError

/**
 * Handles non-user-facing feedback errors (sound/vibration).
 *
 * These should be used for analytics/logging only. UI should stay silent.
 */
interface CounterFeedbackErrorHandler {
    fun onSoundError(error: SoundError)
    fun onVibrationError(error: VibrationError)
}

