package io.droidevs.counterapp.domain.usecases.preference.notification

import io.droidevs.counterapp.domain.preference.notification.NotificationSoundPreference
import kotlinx.coroutines.flow.Flow

class GetNotificationSoundUseCase(private val pref: NotificationSoundPreference) {
    operator fun invoke(): Flow<String> = pref.get()
}

