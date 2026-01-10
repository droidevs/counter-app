package io.droidevs.counterapp.domain.preference

import io.droidevs.counterapp.domain.preference.notification.CounterLimitNotificationPreference
import io.droidevs.counterapp.domain.preference.notification.DailySummaryNotificationPreference
import io.droidevs.counterapp.domain.preference.notification.NotificationSoundPreference
import io.droidevs.counterapp.domain.preference.notification.NotificationVibrationPatternPreference

data class NotificationPreferences(
    val counterLimitNotification: CounterLimitNotificationPreference,
    val dailySummaryNotification: DailySummaryNotificationPreference,
    val notificationSound: NotificationSoundPreference,
    val vibrationPattern: NotificationVibrationPatternPreference
)