package io.droidevs.counterapp.ui.vm.actions

sealed class NotificationPreferenceAction {
    data class SetCounterLimitNotification(val enabled: Boolean) : NotificationPreferenceAction()
    data class SetDailySummaryNotification(val enabled: Boolean) : NotificationPreferenceAction()
    data class SetNotificationSound(val soundUri: String) : NotificationPreferenceAction()
    data class SetNotificationVibrationPattern(val pattern: String) : NotificationPreferenceAction()
}
