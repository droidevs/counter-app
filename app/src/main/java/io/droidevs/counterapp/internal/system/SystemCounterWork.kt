package io.droidevs.counterapp.internal.system

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import io.droidevs.counterapp.domain.system.SystemCounterType
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

    /**
     * Enqueue an increment for a system counter event.
     *
     * @param unique If true, work is enqueued as unique work per counterKey to reduce spam.
     * @param debounceWindowMs If > 0, repeated events within this window are ignored.
     */
    fun enqueueIncrement(
        context: Context,
        counterKey: String,
        unique: Boolean = true,
        debounceWindowMs: Long = defaultDebounceWindowMs(counterKey)
    ) {
        val appContext = context.applicationContext

        // Per-event throttling to prevent double-counting bursts.
        val debouncer = SystemEventDebouncer(appContext)
        if (!debouncer.shouldAccept(eventKey = counterKey, windowMs = debounceWindowMs)) {
            return
        }

        val request = OneTimeWorkRequestBuilder<SystemEventWorker>()
            .setInputData(workDataOf(SystemEventWorker.COUNTER_KEY to counterKey))
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
            .addTag("system_event")
            .addTag("system_event_$counterKey")
            .build()

        val wm = WorkManager.getInstance(appContext)
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

    /**
     * Reasonable defaults per event type.
     *
     * Keep these conservative: we want accuracy but also avoid jitter/double dispatch.
     */
    private fun defaultDebounceWindowMs(counterKey: String): Long {
        val type = SystemCounterType.values().firstOrNull { it.key == counterKey }

        return when (type) {
            // Network events can fire multiple times during one transition.
            SystemCounterType.WIFI_CONNECTIONS,
            SystemCounterType.BLUETOOTH_CONNECTIONS -> 30_000L

            // User-present can fire more than once during unlock/biometrics.
            SystemCounterType.PHONE_UNLOCKS -> 5_000L

            // Calls may report ringing multiple times.
            SystemCounterType.CALLS_RECEIVED -> 15_000L

            // SMS_RECEIVED broadcasts are usually unique, but guard against duplicates.
            SystemCounterType.SMS_RECEIVED -> 2_000L

            // Download complete should be unique per download id, but we don't have id here.
            SystemCounterType.FILES_DOWNLOADED -> 2_000L

            // Power connected can bounce on flaky cables.
            SystemCounterType.BATTERY_CHARGES -> 10_000L

            // Boot/shutdown should basically never repeat, but keep a safety window.
            SystemCounterType.DEVICE_RESTARTS,
            SystemCounterType.DEVICE_SHUTDOWNS -> 60_000L

            else -> 0L
        }
    }
}
