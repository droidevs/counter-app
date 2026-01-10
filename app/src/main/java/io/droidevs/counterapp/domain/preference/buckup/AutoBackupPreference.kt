package io.droidevs.counterapp.domain.preference.buckup

import kotlinx.coroutines.flow.Flow

interface AutoBackupPreference {
    fun get(): Flow<Boolean>
    suspend fun set(value: Boolean)
}