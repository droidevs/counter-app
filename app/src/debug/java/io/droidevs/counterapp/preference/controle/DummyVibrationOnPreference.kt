package io.droidevs.counterapp.preference.controle


import io.droidevs.counterapp.domain.preference.controle.VibrationOnPreference
import io.droidevs.counterapp.preference.DummyPreferenceDelegates
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