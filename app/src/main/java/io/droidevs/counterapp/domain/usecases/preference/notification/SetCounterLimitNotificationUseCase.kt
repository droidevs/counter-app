package io.droidevs.counterapp.domain.usecases.preference.notification

import io.droidevs.counterapp.domain.preference.notification.CounterLimitNotificationPreference

class SetCounterLimitNotificationUseCase(private val pref: CounterLimitNotificationPreference) {
    suspend operator fun invoke(value: Boolean) = pref.set(value)
}

