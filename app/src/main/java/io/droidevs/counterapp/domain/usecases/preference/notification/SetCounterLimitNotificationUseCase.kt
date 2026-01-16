package io.droidevs.counterapp.domain.usecases.preference.notification

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.notification.CounterLimitNotificationPreference
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetCounterLimitNotificationUseCase @Inject constructor(
    private val pref: CounterLimitNotificationPreference,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(value: Boolean) = withContext(dispatchers.io) {
        pref.set(value)
    }
}
