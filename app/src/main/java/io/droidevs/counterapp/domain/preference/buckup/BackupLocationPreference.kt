package io.droidevs.counterapp.domain.preference.buckup

import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow

interface BackupLocationPreference {
    fun get(): Flow<Result<String, PreferenceError>> // Local, Cloud, etc.
    suspend fun set(value: String): Result<Unit, PreferenceError>
}