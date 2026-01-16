package io.droidevs.counterapp.domain.preference.notification

import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow

interface NotificationSoundPreference {
    fun get(): Flow<Result<String, PreferenceError>>
    suspend fun set(value: String): Result<Unit, PreferenceError>
}
