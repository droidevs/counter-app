package io.droidevs.counterapp.domain.preference.buckup

import kotlinx.coroutines.flow.Flow

interface BackupLocationPreference {
    fun get(): Flow<String> // Local, Cloud, etc.
    suspend fun set(value: String)
}