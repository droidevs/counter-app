package io.droidevs.counterapp.domain.preference.notification

import kotlinx.coroutines.flow.Flow

interface CounterLimitNotificationPreference {
    fun get(): Flow<Boolean>
    suspend fun set(value: Boolean)
}