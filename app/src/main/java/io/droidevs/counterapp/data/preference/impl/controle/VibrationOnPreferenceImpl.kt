package io.droidevs.counterapp.data.preference.impl.controle

// VibrationOnPreferenceImpl.kt
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import io.droidevs.counterapp.domain.preference.controle.VibrationOnPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

class VibrationOnPreferenceImpl(
    private val dataStore: DataStore<Preferences>
) : VibrationOnPreference {

    companion object {
        val KEY = booleanPreferencesKey("vibration_enabled")
        const val DEFAULT_VALUE = true
    }

    override fun get(): Flow<Boolean> = dataStore.data
        .map { it[KEY] ?: DEFAULT_VALUE }

    override suspend fun set(value: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY] = value
        }
    }

    suspend fun isEnabled(): Boolean = get().first()
}