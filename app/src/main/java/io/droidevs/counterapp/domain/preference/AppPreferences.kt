package io.droidevs.counterapp.domain.preference

data class AppPreferences(
    val ui: UiAppearancePreferences,
    val counter: CounterBehaviorPreferences,
    val feedback: HardwareFeedbackPreferences,
    val notifications: NotificationPreferences,
    val backup: BackupPreferences
)