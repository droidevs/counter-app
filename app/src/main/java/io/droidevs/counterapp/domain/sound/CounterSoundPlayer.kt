package io.droidevs.counterapp.domain.sound

import io.droidevs.counterapp.domain.result.Result

/**
 * Plays app feedback sounds for counter actions.
 *
 * Implementation lives in app/main (Android SoundPool, MediaPlayer, etc.),
 * but the contract is pure domain.
 */
interface CounterSoundPlayer {
    suspend fun play(action: CounterSoundAction): Result<Unit, SoundError>
}
