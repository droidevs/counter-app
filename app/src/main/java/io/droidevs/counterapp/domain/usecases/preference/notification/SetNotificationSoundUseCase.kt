package io.droidevs.counterapp.domain.usecases.preference.notification

import io.droidevs.counterapp.domain.preference.notification.NotificationSoundPreference

class SetNotificationSoundUseCase(private val pref: NotificationSoundPreference) {
    suspend operator fun invoke(value: String) = pref.set(value)
}

