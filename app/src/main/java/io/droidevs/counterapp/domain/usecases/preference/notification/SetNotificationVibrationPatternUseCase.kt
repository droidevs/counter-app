package io.droidevs.counterapp.domain.usecases.preference.notification

import io.droidevs.counterapp.domain.preference.notification.NotificationVibrationPatternPreference

class SetNotificationVibrationPatternUseCase(private val pref: NotificationVibrationPatternPreference) {
    suspend operator fun invoke(value: String) = pref.set(value)
}

