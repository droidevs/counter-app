package io.droidevs.counterapp.preference.notification

// 4. NotificationVibrationPatternPreference (String - pattern name or custom pattern)
import io.droidevs.counterapp.domain.preference.notification.NotificationVibrationPatternPreference
import io.droidevs.counterapp.preference.DummyPreferenceDelegates
import kotlinx.coroutines.flow.Flow

class DummyNotificationVibrationPatternPreference(
    initialValue: String = "default"   // examples: "default", "short", "long", "none", "heartbeat"
) : NotificationVibrationPatternPreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "notification_vibration_pattern",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<String> = delegate.flow

    override suspend fun set(value: String) {
        delegate.set(value)
    }
}