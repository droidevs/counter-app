package io.droidevs.counterapp.domain.preference.counter

import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow

interface MinimumCounterValuePreference {
    fun get(): Flow<Result<Int?, PreferenceError>>
    suspend fun set(value: Int?): Result<Unit, PreferenceError>
}
