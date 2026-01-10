package io.droidevs.counterapp.data.preference.impl.backup

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import io.droidevs.counterapp.domain.preference.buckup.BackupIntervalPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class BackupIntervalPreferenceImpl(
    private val dataStore: DataStore<Preferences>
) : BackupIntervalPreference {

    companion object {
        val key = longPreferencesKey("backup_interval_hours")

        // Reasonable default: 24 hours = once per day
        const val DEFAULT_VALUE = 24L
    }

    override fun get(): Flow<Long> = dataStore.data
        .map { prefs -> prefs[key] ?: DEFAULT_VALUE }

    override suspend fun set(value: Long) {
        dataStore.edit { prefs ->
            prefs[key] = value
        }
    }

    // Optional but very useful convenience method
    suspend fun getCurrentIntervalHours(): Long = get().first()

    suspend fun isDaily(): Boolean = get().first() == 24L
    suspend fun isWeekly(): Boolean = get().first() == 168L  // 7*24
    suspend fun isMonthly(): Boolean = get().first() == 720L // ~30*24
}