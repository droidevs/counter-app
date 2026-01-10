package io.droidevs.counterapp.data.preference.impl.counter

// CounterIncrementStepPreferenceImpl.kt
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import io.droidevs.counterapp.domain.preference.counter.CounterIncrementStepPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

class CounterIncrementStepPreferenceImpl(
    private val dataStore: DataStore<Preferences>
) : CounterIncrementStepPreference {

    companion object {
        val KEY = intPreferencesKey("counter_increment_step")
        const val DEFAULT_VALUE = 1
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