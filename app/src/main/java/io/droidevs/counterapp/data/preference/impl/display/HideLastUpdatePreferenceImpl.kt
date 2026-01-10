package io.droidevs.counterapp.data.preference.impl.display

// HideLastUpdatePreferenceImpl.kt
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import io.droidevs.counterapp.domain.preference.display.HideLastUpdatePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

class HideLastUpdatePreferenceImpl(
    private val dataStore: DataStore<Preferences>
) : HideLastUpdatePreference {

    companion object {
        val KEY = booleanPreferencesKey("hide_last_update")
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