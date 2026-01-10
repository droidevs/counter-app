package io.droidevs.counterapp.domain.usecases.preference.notification

import io.droidevs.counterapp.domain.preference.notification.NotificationVibrationPatternPreference
import kotlinx.coroutines.flow.Flow

class GetNotificationVibrationPatternUseCase(private val pref: NotificationVibrationPatternPreference) {
    operator fun invoke(): Flow<String> = pref.get()
}

