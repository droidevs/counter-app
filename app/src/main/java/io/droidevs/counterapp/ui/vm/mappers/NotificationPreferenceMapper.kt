package io.droidevs.counterapp.ui.vm.mappers

import io.droidevs.counterapp.ui.vm.states.NotificationPreferenceUiState

fun Quadruple<Boolean, Boolean, String, String>.toNotificationPreferenceUiState(): NotificationPreferenceUiState {
    return NotificationPreferenceUiState(
        counterLimitNotification = first,
        dailySummaryNotification = second,
        notificationSound = third,
        notificationVibrationPattern = fourth
    )
}
