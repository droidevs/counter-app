package io.droidevs.counterapp.internal

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.system.SystemCounterManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SystemCounterSyncWorker(
    context: Context,
    params: WorkerParameters,
    private val systemCounterManager: SystemCounterManager,
    private val repository: CounterRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val counters = systemCounterManager.fetchSystemCounters()

        counters.forEach { key, value ->
            CoroutineScope(Dispatchers.IO).launch {
                repository.updateSystemCounter(key.name, value)
            }
        }
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