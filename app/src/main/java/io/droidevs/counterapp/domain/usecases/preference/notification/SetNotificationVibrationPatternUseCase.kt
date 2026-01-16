package io.droidevs.counterapp.domain.usecases.preference.notification

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.notification.NotificationVibrationPatternPreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetNotificationVibrationPatternUseCase @Inject constructor(
    private val pref: NotificationVibrationPatternPreference,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(value: String): Result<Unit, PreferenceError> = withContext(dispatchers.io) {
        pref.set(value)
    }
}
