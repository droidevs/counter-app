package io.droidevs.counterapp.domain.usecases.preference.notification

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.notification.NotificationSoundPreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetNotificationSoundUseCase @Inject constructor(
    private val pref: NotificationSoundPreference,
    private val dispatchers: DispatcherProvider
) {
    operator fun invoke(): Flow<Result<String, PreferenceError>> = pref.get().flowOn(dispatchers.io)
}
