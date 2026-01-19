package io.droidevs.counterapp.domain.vibration

import io.droidevs.counterapp.domain.result.Result

/** Plays vibration patterns for counter actions. */
interface CounterVibrator {
    suspend fun vibrate(action: CounterVibrationAction): Result<Unit, VibrationError>
}

