package io.droidevs.counterapp.data.preference.impl.counter

// DefaultCounterValuePreferenceImpl.kt
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import io.droidevs.counterapp.domain.preference.counter.DefaultCounterValuePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

class DefaultCounterValuePreferenceImpl(
    private val dataStore: DataStore<Preferences>
) : DefaultCounterValuePreference {

    companion object {
        val KEY = intPreferencesKey("default_counter_value")
        const val DEFAULT_VALUE = 0
    }

    override fun get(): Flow<Int> = dataStore.data
        .map { it[KEY] ?: DEFAULT_VALUE }

    override suspend fun set(value: Int) {
        dataStore.edit { prefs ->
            prefs[KEY] = value
        }
    }

    suspend fun getCurrent(): Int = get().first()
}