package io.droidevs.counterapp.data.preference.impl.backup

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import io.droidevs.counterapp.data.preference.impl.exceptions.flowCatchingPreference
import io.droidevs.counterapp.data.preference.impl.exceptions.runCatchingPreference
import io.droidevs.counterapp.domain.preference.buckup.BackupIntervalPreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class BackupIntervalPreferenceImpl(
    private val dataStore: DataStore<Preferences>
) : BackupIntervalPreference {

    companion object {
        val key = longPreferencesKey("backup_interval_hours")
        const val DEFAULT_VALUE = 24L
    }

    override fun get(): Flow<Result<Long, PreferenceError>> = flowCatchingPreference {
        dataStore.data.map { prefs -> prefs[key] ?: DEFAULT_VALUE }
    }

    override suspend fun set(value: Long): Result<Unit, PreferenceError> = runCatchingPreference {
        dataStore.edit { prefs ->
            prefs[key] = value
        }
    }

    suspend fun getCurrentIntervalHours(): Long = get().first().getOrNull() ?: DEFAULT_VALUE

    suspend fun isDaily(): Boolean = getCurrentIntervalHours() == 24L
    suspend fun isWeekly(): Boolean = getCurrentIntervalHours() == 168L
    suspend fun isMonthly(): Boolean = getCurrentIntervalHours() == 720L
}