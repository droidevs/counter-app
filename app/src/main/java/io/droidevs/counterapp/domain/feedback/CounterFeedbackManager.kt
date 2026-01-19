package io.droidevs.counterapp.domain.feedback

import io.droidevs.counterapp.domain.result.onFailure
import io.droidevs.counterapp.domain.result.resultSuspend
import io.droidevs.counterapp.domain.sound.CounterSoundAction
import io.droidevs.counterapp.domain.usecases.sound.PlayCounterSoundUseCase
import io.droidevs.counterapp.domain.usecases.vibration.VibrateCounterUseCase
import io.droidevs.counterapp.domain.vibration.CounterVibrationAction
import io.droidevs.counterapp.ui.feedback.CounterFeedbackErrorHandler

/**
 * High-level, injectable orchestrator for counter user feedback.
 *
 * It triggers sound and/or vibration for a given action.
 *
 * Notes:
 * - Both sub-actions are internally preference-gated (SoundsOn, VibrationOn).
 * - We intentionally don't fail the whole interaction if one feedback channel fails.
 * - Failures are routed to [CounterFeedbackErrorHandler] for analytics/logging.
 */
class CounterFeedbackManager(
    private val playSound: PlayCounterSoundUseCase,
    private val vibrate: VibrateCounterUseCase,
    private val errorHandler: CounterFeedbackErrorHandler,
) {

    suspend fun onAction(action: CounterFeedbackAction) {
        // Keep UI responsive: feedback is best-effort.
        resultSuspend {
            when (action) {
                CounterFeedbackAction.INCREMENT -> {
                    playSound(CounterSoundAction.INCREMENT)
                        .onFailure { errorHandler.onSoundError(it) }
                    vibrate(CounterVibrationAction.INCREMENT)
                        .onFailure { errorHandler.onVibrationError(it) }
                }

                CounterFeedbackAction.DECREMENT -> {
                    playSound(CounterSoundAction.DECREMENT)
                        .onFailure { errorHandler.onSoundError(it) }
                    vibrate(CounterVibrationAction.DECREMENT)
                        .onFailure { errorHandler.onVibrationError(it) }
                }

                CounterFeedbackAction.RESET -> {
                    playSound(CounterSoundAction.RESET)
                        .onFailure { errorHandler.onSoundError(it) }
                }
            }

            // Best-effort: always succeed.
            io.droidevs.counterapp.domain.result.Result.Success(Unit)
        }
    }
}
