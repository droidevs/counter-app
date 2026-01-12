package io.droidevs.counterapp.internal.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
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
            incrementSystemCounterUseCase(IncrementSystemCounterRequest.of(counterKey))
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        const val COUNTER_KEY = "COUNTER_KEY"
    }
}
