package io.droidevs.counterapp.domain.sound

import io.droidevs.counterapp.domain.result.errors.Error
import java.io.IOException

sealed class SoundError(
    open val cause: Throwable? = null
) : Error {
    data class PlaybackFailed(override val cause: Throwable?) : SoundError(cause)
    data class NotSupported(override val cause: Throwable? = null) : SoundError(cause)

    fun isRetryable(): Boolean = cause is IOException
}
