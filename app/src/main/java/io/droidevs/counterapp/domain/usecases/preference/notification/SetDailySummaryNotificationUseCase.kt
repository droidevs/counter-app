package io.droidevs.counterapp.domain.usecases.preference.notification

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.notification.DailySummaryNotificationPreference
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetDailySummaryNotificationUseCase @Inject constructor(
    private val pref: DailySummaryNotificationPreference,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(value: Boolean) = withContext(dispatchers.io) {
        pref.set(value)
    }
}
