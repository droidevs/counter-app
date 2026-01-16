package io.droidevs.counterapp.ui.vm.mappers

import io.droidevs.counterapp.ui.vm.states.NotificationPreferenceUiState

@Deprecated("This mapper is no longer used and will be removed in a future version.")
fun Quadruple<Boolean, Boolean, String, String>.toNotificationPreferenceUiState(): NotificationPreferenceUiState {
    return NotificationPreferenceUiState(
        counterLimitNotification = first,
        dailySummaryNotification = second,
        notificationSound = third,
        notificationVibrationPattern = fourth
    )
}
