package io.droidevs.counterapp.data.preference.impl.notification

// NotificationVibrationPatternPreferenceImpl.kt
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.droidevs.counterapp.domain.preference.notification.NotificationVibrationPatternPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

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

    override fun get(): Flow<String> = dataStore.data
        .map { it[KEY] ?: DEFAULT_VALUE }

    override suspend fun set(value: String) {
        dataStore.edit { prefs ->
            prefs[KEY] = value
        }
    }

    suspend fun getCurrent(): String = get().first()

    suspend fun isNone(): Boolean = getCurrent() == NONE
}