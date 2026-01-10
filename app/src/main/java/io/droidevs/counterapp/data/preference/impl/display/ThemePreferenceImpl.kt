package io.droidevs.counterapp.data.preference.impl.display

// ThemePreferenceImpl.kt
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.droidevs.counterapp.domain.preference.display.ThemePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

class ThemePreferenceImpl(
    private val dataStore: DataStore<Preferences>
) : ThemePreference {

    companion object {
        val KEY = stringPreferencesKey("app_theme")

        const val DEFAULT_VALUE = "system"
        const val LIGHT = "light"
        const val DARK = "dark"
        const val SYSTEM = "system"
    }

    override fun get(): Flow<String> = dataStore.data
        .map { it[KEY] ?: DEFAULT_VALUE }

    override suspend fun set(value: String) {
        dataStore.edit { prefs ->
            prefs[KEY] = value
        }
    }

    suspend fun getCurrent(): String = get().first()

    suspend fun isDark(): Boolean = getCurrent() == DARK
    suspend fun isLight(): Boolean = getCurrent() == LIGHT
    suspend fun isSystem(): Boolean = getCurrent() == SYSTEM
}