package io.droidevs.counterapp.data.preference.dummy.notification

// 2. DailySummaryNotificationPreference
import io.droidevs.counterapp.data.preference.dummy.DummyPreferenceDelegates
import io.droidevs.counterapp.domain.preference.notification.DailySummaryNotificationPreference
import kotlinx.coroutines.flow.Flow

class DummyDailySummaryNotificationPreference(
    initialValue: Boolean = true
) : DailySummaryNotificationPreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "daily_summary_notification",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<Boolean> = delegate.flow

    override suspend fun set(value: Boolean) {
        delegate.set(value)
    }
}