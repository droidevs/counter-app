package io.droidevs.counterapp.data.sound

import android.util.Log
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.sound.CounterSoundAction
import io.droidevs.counterapp.domain.sound.CounterSoundPlayer
import io.droidevs.counterapp.domain.sound.SoundError
import javax.inject.Inject

/**
 * Dummy implementation: logs the action.
 *
 * Later we can replace with SoundPool + custom audio files.
 */
class DummyCounterSoundPlayer @Inject constructor() : CounterSoundPlayer {
    override suspend fun play(action: CounterSoundAction): Result<Unit, SoundError> {
        Log.d("CounterSound", "play: $action")
        return Result.Success(Unit)
    }
}

