package io.droidevs.counterapp.domain.preference.display

import io.droidevs.counterapp.data.Theme
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow

interface ThemePreference {
    fun get(): Flow<Result<Theme, PreferenceError>>
    suspend fun set(value: Theme): Result<Unit, PreferenceError>
}
