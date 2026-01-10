package io.droidevs.counterapp.data.preference.dummy.display

// 2. HideLastUpdatePreference
import io.droidevs.counterapp.data.preference.dummy.DummyPreferenceDelegates
import io.droidevs.counterapp.domain.preference.display.HideLastUpdatePreference
import kotlinx.coroutines.flow.Flow

class DummyHideLastUpdatePreference(
    initialValue: Boolean = false
) : HideLastUpdatePreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "hide_last_update",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<Boolean> = delegate.flow

    override suspend fun set(value: Boolean) {
        delegate.set(value)
    }
}