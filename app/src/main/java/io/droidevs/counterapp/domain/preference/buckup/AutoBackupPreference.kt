package io.droidevs.counterapp.domain.preference.buckup

import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow

interface AutoBackupPreference {
    fun get(): Flow<Result<Boolean, PreferenceError>>
    suspend fun set(value: Boolean): Result<Unit, PreferenceError>
}