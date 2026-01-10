package io.droidevs.counterapp.domain.usecases.preference.notification

import io.droidevs.counterapp.domain.preference.notification.CounterLimitNotificationPreference
import kotlinx.coroutines.flow.Flow

class GetCounterLimitNotificationUseCase(private val pref: CounterLimitNotificationPreference) {
    operator fun invoke(): Flow<Boolean> = pref.get()
}

