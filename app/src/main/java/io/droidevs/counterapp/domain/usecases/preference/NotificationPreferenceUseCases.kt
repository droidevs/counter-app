package io.droidevs.counterapp.domain.usecases.preference

import io.droidevs.counterapp.domain.usecases.preference.notification.GetCounterLimitNotificationUseCase
import io.droidevs.counterapp.domain.usecases.preference.notification.SetCounterLimitNotificationUseCase
import io.droidevs.counterapp.domain.usecases.preference.notification.GetDailySummaryNotificationUseCase
import io.droidevs.counterapp.domain.usecases.preference.notification.SetDailySummaryNotificationUseCase
import io.droidevs.counterapp.domain.usecases.preference.notification.GetNotificationSoundUseCase
import io.droidevs.counterapp.domain.usecases.preference.notification.SetNotificationSoundUseCase
import io.droidevs.counterapp.domain.usecases.preference.notification.GetNotificationVibrationPatternUseCase
import io.droidevs.counterapp.domain.usecases.preference.notification.SetNotificationVibrationPatternUseCase

data class NotificationPreferenceUseCases(
    val getCounterLimitNotification: GetCounterLimitNotificationUseCase,
    val setCounterLimitNotification: SetCounterLimitNotificationUseCase,
    val getDailySummaryNotification: GetDailySummaryNotificationUseCase,
    val setDailySummaryNotification: SetDailySummaryNotificationUseCase,
    val getNotificationSound: GetNotificationSoundUseCase,
    val setNotificationSound: SetNotificationSoundUseCase,
    val getNotificationVibrationPattern: GetNotificationVibrationPatternUseCase,
    val setNotificationVibrationPattern: SetNotificationVibrationPatternUseCase
)
