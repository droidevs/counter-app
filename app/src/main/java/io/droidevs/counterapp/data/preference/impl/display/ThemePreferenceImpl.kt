package io.droidevs.counterapp.data.preference.impl.display

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.droidevs.counterapp.data.Theme
import io.droidevs.counterapp.data.preference.impl.exceptions.flowCatchingPreference
import io.droidevs.counterapp.data.preference.impl.exceptions.runCatchingPreference
import io.droidevs.counterapp.domain.preference.display.ThemePreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ThemePreferenceImpl(
    private val dataStore: DataStore<Preferences>
) : ThemePreference {

    companion object {
        val KEY = stringPreferencesKey("app_theme")
    }

    override fun get(): Flow<Result<Theme, PreferenceError>> = flowCatchingPreference {
        dataStore.data
            .map { it[KEY] ?: Theme.SYSTEM.name }.map { Theme.valueOf(it) }
    }

    override suspend fun set(value: Theme): Result<Unit, PreferenceError> = runCatchingPreference {
        dataStore.edit { prefs ->
            prefs[KEY] = value.name
        }
    }

    suspend fun getCurrent(): Theme = get().first().getOrNull() ?: Theme.SYSTEM

    suspend fun isDark(): Boolean = getCurrent() == Theme.DARK
    suspend fun isLight(): Boolean = getCurrent() == Theme.LIGHT
    suspend fun isSystem(): Boolean = getCurrent() == Theme.SYSTEM
}