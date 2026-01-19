package io.droidevs.counterapp.domain.vibration

import io.droidevs.counterapp.domain.result.errors.Error

sealed class VibrationError : Error {
    data object NotSupported : VibrationError()
    data class Failed(val cause: Throwable? = null) : VibrationError()
}

