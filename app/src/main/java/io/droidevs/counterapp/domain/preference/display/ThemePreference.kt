package io.droidevs.counterapp.domain.preference.display

import kotlinx.coroutines.flow.Flow

interface ThemePreference {
    fun get(): Flow<String>
    suspend fun set(value: String)
}