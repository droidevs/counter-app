package io.droidevs.counterapp.internal.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import io.droidevs.counterapp.domain.usecases.counters.IncrementSystemCounterUseCase
import io.droidevs.counterapp.domain.usecases.requests.IncrementSystemCounterRequest

@HiltWorker
class SystemEventWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val incrementSystemCounterUseCase: IncrementSystemCounterUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val counterKey = inputData.getString(COUNTER_KEY)
            ?: return Result.failure()

        return try {
            val domainResult = incrementSystemCounterUseCase(IncrementSystemCounterRequest.of(counterKey))

            when (domainResult) {
                is io.droidevs.counterapp.domain.result.Result.Success -> Result.success()
                is io.droidevs.counterapp.domain.result.Result.Failure -> {
                    when (val error = domainResult.error) {
                        is DatabaseError.DatabaseLocked -> if (error.retryable) Result.retry() else Result.failure()
                        is DatabaseError.TimeOut -> Result.retry()
                        is DatabaseError.TransientError -> Result.retry()
                        // Anything else is considered non-retryable.
                        else -> Result.failure()
                    }
                }
            }
        } catch (_: Throwable) {
            // If we got an unexpected crash here, retry once WorkManager applies backoff.
            Result.retry()
        }
    }

    companion object {
        const val COUNTER_KEY = "COUNTER_KEY"
    }
}
