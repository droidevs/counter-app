package io.droidevs.counterapp.domain.usecases.preference.notification

import io.droidevs.counterapp.domain.preference.notification.DailySummaryNotificationPreference

class SetDailySummaryNotificationUseCase(private val pref: DailySummaryNotificationPreference) {
    suspend operator fun invoke(value: Boolean) = pref.set(value)
}

