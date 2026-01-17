package io.droidevs.counterapp.internal

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.system.SystemCounterManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@HiltWorker
class SystemCounterSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val systemCounterManager: SystemCounterManager,
    private val repository: CounterRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val counters = systemCounterManager.fetchSystemCounters()

            // Run sequentially so WorkManager doesn't mark the work successful before updates finish.
            counters.forEach { (type, value) ->
                repository.updateSystemCounter(type.key, value)
            }

            Result.success()
        } catch (_: Throwable) {
            // Let WorkManager retry on transient issues.
            Result.retry()
        }
    }

    companion object {
        const val UNIQUE_WORK_NAME = "system_counter_sync"
        const val TAG = "system_counter_sync"
    }
}

fun scheduleSystemCounterSync(context: Context) {
    val constraints = Constraints.Builder()
        // Sync only when there is network; safe default even if some trackers are local.
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val workRequest = PeriodicWorkRequestBuilder<SystemCounterSyncWorker>(
        repeatInterval = 15,
        repeatIntervalTimeUnit = TimeUnit.MINUTES
    )
        .setConstraints(constraints)
        .addTag(SystemCounterSyncWorker.TAG)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        SystemCounterSyncWorker.UNIQUE_WORK_NAME,
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}