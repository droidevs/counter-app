package io.droidevs.counterapp.internal.notification

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import io.droidevs.counterapp.domain.notification.NotificationEventType
import io.droidevs.counterapp.internal.worker.NotificationWorker

class AppNotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        enqueueWork(sbn, NotificationEventType.POSTED)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        enqueueWork(sbn, NotificationEventType.REMOVED)
    }

    private fun enqueueWork(
        sbn: StatusBarNotification,
        type: NotificationEventType
    ) {
        val work = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(
                workDataOf(
                    "EVENT_TYPE" to type.name
                )
            )
            .build()

        WorkManager.getInstance(this).enqueue(work)
    }
}
