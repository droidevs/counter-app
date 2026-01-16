package io.droidevs.counterapp.domain.usecases.preference.notification

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.notification.NotificationVibrationPatternPreference
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetNotificationVibrationPatternUseCase @Inject constructor(
    private val pref: NotificationVibrationPatternPreference,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(value: String) = withContext(dispatchers.io) {
        pref.set(value)
    }
}
