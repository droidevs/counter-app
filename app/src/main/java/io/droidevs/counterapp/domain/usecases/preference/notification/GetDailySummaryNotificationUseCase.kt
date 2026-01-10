package io.droidevs.counterapp.domain.usecases.preference.notification

import io.droidevs.counterapp.domain.preference.notification.DailySummaryNotificationPreference
import kotlinx.coroutines.flow.Flow

class GetDailySummaryNotificationUseCase(private val pref: DailySummaryNotificationPreference) {
    operator fun invoke(): Flow<Boolean> = pref.get()
}

