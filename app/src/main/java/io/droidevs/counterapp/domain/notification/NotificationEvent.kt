package io.droidevs.counterapp.domain.notification

data class NotificationEvent(
    val packageName: String,
    val notificationId: Int,
    val eventType: NotificationEventType,
    val timestamp: Long
)
