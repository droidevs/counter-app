package io.droidevs.counterapp.internal.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.droidevs.counterapp.domain.notification.NotificationEventType
import io.droidevs.counterapp.domain.system.SystemCounterType
import io.droidevs.counterapp.domain.usecases.counters.IncrementSystemCounterUseCase
import io.droidevs.counterapp.domain.usecases.requests.IncrementSystemCounterRequest

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val incrementSystemCounterUseCase: IncrementSystemCounterUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val eventType = NotificationEventType.valueOf(
            inputData.getString("EVENT_TYPE")!!
        )

        val counterKey = when (eventType) {
            NotificationEventType.POSTED -> SystemCounterType.NOTIFICATIONS_RECEIVED.key
            NotificationEventType.REMOVED -> SystemCounterType.NOTIFICATIONS_CLEARED.key
        }

        incrementSystemCounterUseCase.invoke(IncrementSystemCounterRequest.of(counterKey))

        return Result.success()
    }
}
