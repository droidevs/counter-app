package io.droidevs.counterapp.domain.usecases.preference.notification

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.notification.CounterLimitNotificationPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCounterLimitNotificationUseCase @Inject constructor(
    private val pref: CounterLimitNotificationPreference,
    private val dispatchers: DispatcherProvider
) {
    operator fun invoke(): Flow<Boolean> = pref.get().flowOn(dispatchers.io)
}
