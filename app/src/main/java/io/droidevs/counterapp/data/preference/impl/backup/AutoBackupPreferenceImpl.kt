package io.droidevs.counterapp.data.preference.impl.backup

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import io.droidevs.counterapp.domain.preference.buckup.AutoBackupPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AutoBackupPreferenceImpl(
    private val dataStore: DataStore<Preferences>
) : AutoBackupPreference {

    companion object {
        val key = booleanPreferencesKey("auto_backup_enabled")

        // You can also expose default value as constant if you like
        const val DEFAULT_VALUE = true
    }

    override fun get(): Flow<Boolean> = dataStore.data
        .map { prefs -> prefs[key] ?: DEFAULT_VALUE }

    override suspend fun set(value: Boolean) {
        dataStore.edit { prefs ->
            prefs[key] = value
        }
    }

    // Very common convenience method
    suspend fun isEnabled(): Boolean = get().first()
}