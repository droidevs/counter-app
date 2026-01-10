package io.droidevs.counterapp.data.preference.impl.display

// HideControlsPreferenceImpl.kt
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import io.droidevs.counterapp.domain.preference.display.HideControlsPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

class HideControlsPreferenceImpl(
    private val dataStore: DataStore<Preferences>
) : HideControlsPreference {

    companion object {
        val KEY = booleanPreferencesKey("hide_controls")
        const val DEFAULT_VALUE = false
    }

    override fun get(): Flow<Boolean> = dataStore.data
        .map { it[KEY] ?: DEFAULT_VALUE }

    override suspend fun set(value: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY] = value
        }
    }

    suspend fun isHidden(): Boolean = get().first()
}