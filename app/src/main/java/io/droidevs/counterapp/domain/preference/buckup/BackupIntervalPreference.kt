package io.droidevs.counterapp.domain.preference.buckup

import kotlinx.coroutines.flow.Flow

interface BackupIntervalPreference {
    fun get(): Flow<Long> // Hours
    suspend fun set(value: Long)
}