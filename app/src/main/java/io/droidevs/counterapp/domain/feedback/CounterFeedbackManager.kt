package io.droidevs.counterapp.domain.feedback

import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.RootError
import io.droidevs.counterapp.domain.result.onFailure
import io.droidevs.counterapp.domain.result.resultSuspend
import io.droidevs.counterapp.domain.sound.CounterSoundAction
import io.droidevs.counterapp.domain.sound.SoundError
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
    private val vibrate: VibrateCounterUseCase
) {

    suspend fun onAction(action: CounterFeedbackAction) : Result<Unit, RootError> = resultSuspend {
        when (action) {
            CounterFeedbackAction.INCREMENT -> {
                playSound(CounterSoundAction.INCREMENT)
                vibrate(CounterVibrationAction.INCREMENT)
            }

            CounterFeedbackAction.DECREMENT -> {
                playSound(CounterSoundAction.DECREMENT)
                vibrate(CounterVibrationAction.DECREMENT)
            }

            CounterFeedbackAction.RESET -> {
                playSound(CounterSoundAction.RESET)
            }
        }
        Result.Success(Unit)
    }
}
