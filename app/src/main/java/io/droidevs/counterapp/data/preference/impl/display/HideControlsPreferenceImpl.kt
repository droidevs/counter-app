package io.droidevs.counterapp.data.preference.impl.display

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import io.droidevs.counterapp.data.preference.impl.exceptions.flowCatchingPreference
import io.droidevs.counterapp.data.preference.impl.exceptions.runCatchingPreference
import io.droidevs.counterapp.domain.preference.display.HideControlsPreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class HideControlsPreferenceImpl(
    private val dataStore: DataStore<Preferences>
) : HideControlsPreference {

    companion object {
        val KEY = booleanPreferencesKey("hide_controls")
        const val DEFAULT_VALUE = false
    }

    override fun get(): Flow<Result<Boolean, PreferenceError>> = flowCatchingPreference {
        dataStore.data.map { it[KEY] ?: DEFAULT_VALUE }
    }

    override suspend fun set(value: Boolean): Result<Unit, PreferenceError> = runCatchingPreference {
        dataStore.edit { prefs ->
            prefs[KEY] = value
        }
    }

    suspend fun isHidden(): Boolean = get().first().getOrNull() ?: DEFAULT_VALUE
}