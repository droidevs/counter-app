package io.droidevs.counterapp.data.preference.impl.backup

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import io.droidevs.counterapp.data.preference.impl.exceptions.flowCatchingPreference
import io.droidevs.counterapp.data.preference.impl.exceptions.runCatchingPreference
import io.droidevs.counterapp.domain.preference.buckup.AutoBackupPreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AutoBackupPreferenceImpl(
    private val dataStore: DataStore<Preferences>
) : AutoBackupPreference {

    companion object {
        val key = booleanPreferencesKey("auto_backup_enabled")
        const val DEFAULT_VALUE = true
    }

    override fun get(): Flow<Result<Boolean, PreferenceError>> = flowCatchingPreference {
        dataStore.data.map { prefs -> prefs[key] ?: DEFAULT_VALUE }
    }

    override suspend fun set(value: Boolean): Result<Unit, PreferenceError> = runCatchingPreference {
        dataStore.edit { prefs ->
            prefs[key] = value
        }
    }

    suspend fun isEnabled(): Boolean = get().first().getOrNull() ?: DEFAULT_VALUE
}