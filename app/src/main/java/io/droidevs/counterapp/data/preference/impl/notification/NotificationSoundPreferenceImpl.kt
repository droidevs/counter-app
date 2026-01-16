package io.droidevs.counterapp.data.preference.impl.notification

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.droidevs.counterapp.data.preference.impl.exceptions.flowCatchingPreference
import io.droidevs.counterapp.data.preference.impl.exceptions.runCatchingPreference
import io.droidevs.counterapp.domain.preference.notification.NotificationSoundPreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class NotificationSoundPreferenceImpl(
    private val dataStore: DataStore<Preferences>
) : NotificationSoundPreference {

    companion object {
        val KEY = stringPreferencesKey("notification_sound")

        const val DEFAULT_VALUE = "default"
        const val NONE = "none"
        const val BEEP = "beep"
        const val CHIME = "chime"
    }

    override fun get(): Flow<Result<String, PreferenceError>> = flowCatchingPreference {
        dataStore.data.map { it[KEY] ?: DEFAULT_VALUE }
    }

    override suspend fun set(value: String): Result<Unit, PreferenceError> = runCatchingPreference {
        dataStore.edit { prefs ->
            prefs[KEY] = value
        }
    }

    suspend fun getCurrent(): String = get().first().getOrNull() ?: DEFAULT_VALUE
}