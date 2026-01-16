package io.droidevs.counterapp.data.preference.impl.counter

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import io.droidevs.counterapp.data.preference.impl.exceptions.flowCatchingPreference
import io.droidevs.counterapp.data.preference.impl.exceptions.runCatchingPreference
import io.droidevs.counterapp.domain.preference.counter.CounterDecrementStepPreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CounterDecrementStepPreferenceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : CounterDecrementStepPreference {

    private val key = intPreferencesKey("counter_decrement_step")

    override fun get(): Flow<Result<Int, PreferenceError>> = flowCatchingPreference {
        dataStore.data.map { preferences ->
            preferences[key] ?: 1
        }
    }

    override suspend fun set(value: Int): Result<Unit, PreferenceError> = runCatchingPreference {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}
