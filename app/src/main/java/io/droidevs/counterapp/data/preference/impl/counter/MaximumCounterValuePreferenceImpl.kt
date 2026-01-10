package io.droidevs.counterapp.data.preference.impl.counter

// MaximumCounterValuePreferenceImpl.kt
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import io.droidevs.counterapp.domain.preference.counter.MaximumCounterValuePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

class MaximumCounterValuePreferenceImpl(
    private val dataStore: DataStore<Preferences>
) : MaximumCounterValuePreference {

    companion object {
        val KEY = intPreferencesKey("counter_maximum_value")
        // null = no maximum → we use null as default
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