package io.droidevs.counterapp.preference.notification

// 3. NotificationSoundPreference (String - usually sound file name or uri)
import io.droidevs.counterapp.domain.preference.notification.NotificationSoundPreference
import io.droidevs.counterapp.preference.DummyPreferenceDelegates
import kotlinx.coroutines.flow.Flow

class DummyNotificationSoundPreference(
    initialValue: String = "default"   // common values: "default", "none", "beep", etc.
) : NotificationSoundPreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "notification_sound",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<String> = delegate.flow

    override suspend fun set(value: String) {
        delegate.set(value)
    }
}