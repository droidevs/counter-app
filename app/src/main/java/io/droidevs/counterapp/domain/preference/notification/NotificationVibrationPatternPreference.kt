package io.droidevs.counterapp.domain.preference.notification

import kotlinx.coroutines.flow.Flow

interface NotificationVibrationPatternPreference {
    fun get(): Flow<String>
    suspend fun set(value: String)
}