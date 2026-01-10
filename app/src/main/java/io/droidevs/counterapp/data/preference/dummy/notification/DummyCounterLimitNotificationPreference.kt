package io.droidevs.counterapp.data.preference.dummy.notification

// 1. CounterLimitNotificationPreference
import io.droidevs.counterapp.data.preference.dummy.DummyPreferenceDelegates
import io.droidevs.counterapp.domain.preference.notification.CounterLimitNotificationPreference
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