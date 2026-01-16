package io.droidevs.counterapp.domain.usecases.preference.notification

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.notification.NotificationVibrationPatternPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetNotificationVibrationPatternUseCase @Inject constructor(
    private val pref: NotificationVibrationPatternPreference,
    private val dispatchers: DispatcherProvider
) {
    operator fun invoke(): Flow<String> = pref.get().flowOn(dispatchers.io)
}
