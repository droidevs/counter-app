package io.droidevs.counterapp.internal.system

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import io.droidevs.counterapp.internal.worker.SystemEventWorker
import java.util.concurrent.TimeUnit

/**
 * Centralized helper for enqueuing system-counter event work.
 *
 * Why: All BroadcastReceivers should be lightweight + consistent.
 * This reduces duplication and makes it easier to add debouncing/rate-limits.
 */
object SystemCounterWork {

    private const val UNIQUE_PREFIX = "system_event_"

    fun enqueueIncrement(
        context: Context,
        counterKey: String,
        unique: Boolean = true
    ) {
        val request = OneTimeWorkRequestBuilder<SystemEventWorker>()
            .setInputData(workDataOf(SystemEventWorker.COUNTER_KEY to counterKey))
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
            .addTag("system_event")
            .addTag("system_event_$counterKey")
            .build()

        val wm = WorkManager.getInstance(context)
        if (unique) {
            wm.enqueueUniqueWork(
                UNIQUE_PREFIX + counterKey,
                ExistingWorkPolicy.APPEND_OR_REPLACE,
                request
            )
        } else {
            wm.enqueue(request)
        }
    }
}


