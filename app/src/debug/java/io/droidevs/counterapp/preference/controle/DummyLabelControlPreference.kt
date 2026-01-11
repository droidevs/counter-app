package io.droidevs.counterapp.preference.controle


import io.droidevs.counterapp.domain.preference.controle.LabelControlPreference
import io.droidevs.counterapp.preference.DummyPreferenceDelegates
import kotlinx.coroutines.flow.Flow

class DummyLabelControlPreference(
    initialValue: Boolean = false
) : LabelControlPreference {
    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate("label_control", initialValue)
    }

    override fun get(): Flow<Boolean> = delegate.flow
    override suspend fun set(value: Boolean) = delegate.set(value)
}