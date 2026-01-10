package io.droidevs.counterapp.domain.preference.display

import kotlinx.coroutines.flow.Flow

interface KeepScreenOnPreference {
    fun get(): Flow<Boolean>
    suspend fun set(value: Boolean)
}