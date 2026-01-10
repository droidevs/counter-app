package io.droidevs.counterapp.data.preference.impl.controle

// SoundsOnPreferenceImpl.kt
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import io.droidevs.counterapp.domain.preference.controle.SoundsOnPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

class SoundsOnPreferenceImpl(
    private val dataStore: DataStore<Preferences>
) : SoundsOnPreference {

    companion object {
        val KEY = booleanPreferencesKey("sounds_enabled")
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