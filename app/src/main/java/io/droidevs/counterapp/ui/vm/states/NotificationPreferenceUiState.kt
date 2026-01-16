package io.droidevs.counterapp.ui.vm.states

data class NotificationPreferenceUiState(
    val isLoading: Boolean = false,
    val counterLimitNotification: Boolean = false,
    val dailySummaryNotification: Boolean = false,
    val notificationSound: String = "",
    val notificationVibrationPattern: String = "",
    val error: Boolean = false
)
