package io.droidevs.counterapp.preference.controle

import io.droidevs.counterapp.data.preference.dummy.DummyPreferenceDelegates
import io.droidevs.counterapp.domain.preference.controle.VibrationOnPreference
import kotlinx.coroutines.flow.Flow

class DummyVibrationOnPreference(
    initialValue: Boolean = true
) : VibrationOnPreference {
    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate("vibration_enabled", initialValue)
    }

    override fun get(): Flow<Boolean> = delegate.flow
    override suspend fun set(value: Boolean) = delegate.set(value)
}