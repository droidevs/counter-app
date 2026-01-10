package io.droidevs.counterapp.domain.preference.notification

import kotlinx.coroutines.flow.Flow

interface NotificationSoundPreference {
    fun get(): Flow<String>
    suspend fun set(value: String)
}