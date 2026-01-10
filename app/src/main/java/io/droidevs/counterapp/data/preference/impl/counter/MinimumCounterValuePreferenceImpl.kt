package io.droidevs.counterapp.data.preference.impl.counter

// MinimumCounterValuePreferenceImpl.kt
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import io.droidevs.counterapp.domain.preference.counter.MinimumCounterValuePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

class MinimumCounterValuePreferenceImpl(
    private val dataStore: DataStore<Preferences>
) : MinimumCounterValuePreference {

    companion object {
        val KEY = intPreferencesKey("counter_minimum_value")
        // null = no minimum → we use null as default
    }

    override fun get(): Flow<Int?> = dataStore.data
        .map { it[KEY] }

    override suspend fun set(value: Int?) {
        dataStore.edit { prefs ->
            if (value != null) {
                prefs[KEY] = value
            } else {
                prefs.remove(KEY)
            }
        }
    }

    suspend fun getCurrent(): Int? = get().first()
}