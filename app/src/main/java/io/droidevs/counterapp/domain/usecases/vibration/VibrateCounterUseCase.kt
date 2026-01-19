package io.droidevs.counterapp.domain.usecases.vibration

import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.recoverWith
import io.droidevs.counterapp.domain.result.resultSuspend
import io.droidevs.counterapp.domain.result.resultSuspendFromFlow
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import io.droidevs.counterapp.domain.usecases.preference.controle.GetVibrationOnUseCase
import io.droidevs.counterapp.domain.vibration.CounterVibrationAction
import io.droidevs.counterapp.domain.vibration.CounterVibrator
import io.droidevs.counterapp.domain.vibration.VibrationError
import javax.inject.Inject

/**
 * High-level entry point for counter vibration.
 *
 * Rules:
 * - If preference read fails, default to vibration enabled.
 * - If disabled, noop.
 * - Only real vibration errors propagate.
 */
class VibrateCounterUseCase @Inject constructor(
    private val getVibrationOn: GetVibrationOnUseCase,
    private val vibrator: CounterVibrator,
) {
    suspend operator fun invoke(action: CounterVibrationAction): Result<Unit, VibrationError> = resultSuspend {
        val enabledResult: Result<Boolean, PreferenceError> = resultSuspendFromFlow {
            getVibrationOn().recoverWith { Result.Success(true) }
        }

        val enabled = enabledResult.fold(
            onSuccess = { it },
            onFailure = { true }
        )

        if (!enabled) {
            Result.Success(Unit)
        } else {
            vibrator.vibrate(action)
        }
    }
}

