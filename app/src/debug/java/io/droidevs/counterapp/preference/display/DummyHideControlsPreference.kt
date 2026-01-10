package io.droidevs.counterapp.preference.display

// 1. HideControlsPreference
import io.droidevs.counterapp.data.preference.dummy.DummyPreferenceDelegates
import io.droidevs.counterapp.domain.preference.display.HideControlsPreference
import kotlinx.coroutines.flow.Flow

class DummyHideControlsPreference(
    initialValue: Boolean = false
) : HideControlsPreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "hide_controls",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<Boolean> = delegate.flow

    override suspend fun set(value: Boolean) {
        delegate.set(value)
    }
}