package io.droidevs.counterapp.data.preference.impl.notification

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.droidevs.counterapp.data.preference.impl.exceptions.flowCatchingPreference
import io.droidevs.counterapp.data.preference.impl.exceptions.runCatchingPreference
import io.droidevs.counterapp.domain.preference.notification.NotificationVibrationPatternPreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class NotificationVibrationPatternPreferenceImpl(
    private val dataStore: DataStore<Preferences>
) : NotificationVibrationPatternPreference {

    companion object {
        val KEY = stringPreferencesKey("notification_vibration_pattern")

        const val DEFAULT_VALUE = "default"
        const val NONE = "none"
        const val SHORT = "short"
        const val LONG = "long"
        const val HEARTBEAT = "heartbeat"
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

    suspend fun isNone(): Boolean = getCurrent() == NONE
}