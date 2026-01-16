package io.droidevs.counterapp.preference.notification

import io.droidevs.counterapp.domain.preference.notification.DailySummaryNotificationPreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.asSuccess
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import io.droidevs.counterapp.preference.DummyPreferenceDelegates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DummyDailySummaryNotificationPreference(
    initialValue: Boolean = true
) : DailySummaryNotificationPreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "daily_summary_notification",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<Result<Boolean, PreferenceError>> = delegate.flow.map { it.asSuccess() }

    override suspend fun set(value: Boolean): Result<Unit, PreferenceError> {
        delegate.set(value)
        return Unit.asSuccess()
    }
}
