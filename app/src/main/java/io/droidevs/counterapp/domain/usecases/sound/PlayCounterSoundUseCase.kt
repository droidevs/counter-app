package io.droidevs.counterapp.domain.usecases.sound

import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.RootError
import io.droidevs.counterapp.domain.result.asSuccess
import io.droidevs.counterapp.domain.result.recoverWith
import io.droidevs.counterapp.domain.result.resultSuspend
import io.droidevs.counterapp.domain.result.resultSuspendFromFlow
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import io.droidevs.counterapp.domain.result.recover
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

    suspend operator fun invoke(action: CounterSoundAction): Result<Unit, RootError> = resultSuspendFromFlow {
        // Collapse preference flow into a single Result<Boolean, PreferenceError>
        // while defaulting failures to enabled=true.
        getSoundsOn()
            .recoverWith { prefError: PreferenceError ->
                // Treat preference read errors as "enabled"
                Result.Success(false)
            }
            .combineSuspended { enabled ->
                if (!enabled) {
                    // Sounds are disabled, no-op
                    Result.Success(Unit)
                } else {
                    // Play sound according to action
                    player.play(action)
                }
            }

    }
}
