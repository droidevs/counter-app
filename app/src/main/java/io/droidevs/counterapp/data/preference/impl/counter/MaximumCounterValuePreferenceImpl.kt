package io.droidevs.counterapp.data.preference.impl.counter

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import io.droidevs.counterapp.data.preference.impl.exceptions.flowCatchingPreference
import io.droidevs.counterapp.data.preference.impl.exceptions.runCatchingPreference
import io.droidevs.counterapp.domain.preference.counter.MaximumCounterValuePreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class MaximumCounterValuePreferenceImpl(
    private val dataStore: DataStore<Preferences>
) : MaximumCounterValuePreference {

    companion object {
        val KEY = intPreferencesKey("counter_maximum_value")
    }

    override fun get(): Flow<Result<Int?, PreferenceError>> = flowCatchingPreference {
        dataStore.data.map { it[KEY] }
    }

    override suspend fun set(value: Int?): Result<Unit, PreferenceError> = runCatchingPreference {
        dataStore.edit { prefs ->
            if (value != null) {
                prefs[KEY] = value
            } else {
                prefs.remove(KEY)
            }
        }
    }

    suspend fun getCurrent(): Int? = get().first().getOrNull()
}