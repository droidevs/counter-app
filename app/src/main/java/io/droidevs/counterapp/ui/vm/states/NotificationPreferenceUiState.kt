package io.droidevs.counterapp.ui.vm.states

data class NotificationPreferenceUiState(
    val counterLimitNotification: Boolean = false,
    val dailySummaryNotification: Boolean = false,
    val notificationSound: String = "",
    val notificationVibrationPattern: String = ""
)
