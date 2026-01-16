package io.droidevs.counterapp.domain.usecases.preference.notification

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.notification.DailySummaryNotificationPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetDailySummaryNotificationUseCase @Inject constructor(
    private val pref: DailySummaryNotificationPreference,
    private val dispatchers: DispatcherProvider
) {
    operator fun invoke(): Flow<Boolean> = pref.get().flowOn(dispatchers.io)
}
