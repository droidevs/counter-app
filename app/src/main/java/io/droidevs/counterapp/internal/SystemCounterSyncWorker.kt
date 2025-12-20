package io.droidevs.counterapp.internal

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import io.droidevs.counterapp.domain.system.SystemCounterManager
import java.util.concurrent.TimeUnit

class SystemCounterSyncWorker(
    context: Context,
    params: WorkerParameters,
    private val systemCounterManager: SystemCounterManager
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val counters = systemCounterManager.fetchSystemCounters()
        // TODO : save/ update counters in my db
        // todo : create a new repo named SystemCounterUpdater and use it to update the counter by defining
        // cutom functions to update all counters
        return Result.success()
    }
}

fun scheduleSystemCounterSync(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<SystemCounterSyncWorker>(
        repeatInterval = 15,
        repeatIntervalTimeUnit = TimeUnit.MINUTES
    ).build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "system_counter_sync",
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}