package io.droidevs.counterapp.data.preference.impl.notification

// DailySummaryNotificationPreferenceImpl.kt
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import io.droidevs.counterapp.domain.preference.notification.DailySummaryNotificationPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

class DailySummaryNotificationPreferenceImpl(
    private val dataStore: DataStore<Preferences>
) : DailySummaryNotificationPreference {

    companion object {
        val KEY = booleanPreferencesKey("daily_summary_notification")
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