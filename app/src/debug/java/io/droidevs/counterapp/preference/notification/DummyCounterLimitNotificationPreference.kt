package io.droidevs.counterapp.preference.notification


import io.droidevs.counterapp.domain.preference.notification.CounterLimitNotificationPreference
import io.droidevs.counterapp.preference.DummyPreferenceDelegates
import kotlinx.coroutines.flow.Flow

class DummyCounterLimitNotificationPreference(
    initialValue: Boolean = true
) : CounterLimitNotificationPreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "counter_limit_notification",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<Boolean> = delegate.flow

    override suspend fun set(value: Boolean) {
        delegate.set(value)
    }
}