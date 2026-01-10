package io.droidevs.counterapp.domain.preference.display

import io.droidevs.counterapp.data.Theme
import kotlinx.coroutines.flow.Flow

interface ThemePreference {
    fun get(): Flow<Theme>
    suspend fun set(value: Theme)
}