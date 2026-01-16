package io.droidevs.counterapp.domain.preference.buckup

import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow

interface BackupIntervalPreference {
    fun get(): Flow<Result<Long, PreferenceError>> // Hours
    suspend fun set(value: Long): Result<Unit, PreferenceError>
}