package io.droidevs.counterapp.data.preference.dummy.controle

import io.droidevs.counterapp.data.preference.dummy.DummyPreferenceDelegates
import io.droidevs.counterapp.domain.preference.controle.HardwareButtonControlPreference
import kotlinx.coroutines.flow.Flow

// Hardware Button Control - using delegation
class DummyHardwareButtonControlPreference(
    initialValue: Boolean = false
) : HardwareButtonControlPreference {

    // Lazy delegate - created only when first accessed
    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            "hardware_button_control",
            initialValue
        )
    }

    override fun get(): Flow<Boolean> = delegate.flow

    override suspend fun set(value: Boolean) = delegate.set(value)

    fun isEnabled(): Boolean = delegate.getCurrent()
}