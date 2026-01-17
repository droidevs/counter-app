package io.droidevs.counterapp.preference.notification

import io.droidevs.counterapp.domain.preference.notification.NotificationSoundPreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import io.droidevs.counterapp.preference.DummyPreferenceDelegates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DummyNotificationSoundPreference(
    initialValue: String = "default"
) : NotificationSoundPreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "notification_sound",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<Result<String, PreferenceError>> = delegate.flow.map { Result.Success(it) }

    override suspend fun set(value: String): Result<Unit, PreferenceError> {
        delegate.set(value)
        return Result.Success(Unit)
    }
}
