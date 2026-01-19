package io.droidevs.counterapp.domain.usecases.sound

import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.recoverWith
import io.droidevs.counterapp.domain.result.resultSuspend
import io.droidevs.counterapp.domain.result.resultSuspendFromFlow
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import io.droidevs.counterapp.domain.sound.CounterSoundAction
import io.droidevs.counterapp.domain.sound.CounterSoundPlayer
import io.droidevs.counterapp.domain.sound.SoundError
import io.droidevs.counterapp.domain.usecases.preference.controle.GetSoundsOnUseCase
import javax.inject.Inject

/**
 * OOP entry point for playing counter sounds.
 *
 * Rules:
 * - Preference read failures are treated as "enabled" (to keep UX responsive).
 * - Only sound playback errors are allowed to propagate.
 */
class PlayCounterSoundUseCase @Inject constructor(
    private val getSoundsOn: GetSoundsOnUseCase,
    private val player: CounterSoundPlayer,
) {

    suspend operator fun invoke(action: CounterSoundAction): Result<Unit, SoundError> = resultSuspend {
        val enabledResult: Result<Boolean, PreferenceError> = resultSuspendFromFlow {
            // default-to-enabled on preference failures
            getSoundsOn().recoverWith { Result.Success(true) }
        }

        val enabled: Boolean = enabledResult.fold(
            onSuccess = { it },
            onFailure = { true }
        )

        if (!enabled) {
            Result.Success(Unit)
        } else {
            player.play(action)
        }
    }
}
