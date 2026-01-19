package io.droidevs.counterapp.domain.usecases.history

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.history.HistoryEventRecorder
import io.droidevs.counterapp.domain.model.HistoryEvent
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddHistoryEventUseCase @Inject constructor(
    private val recorder: HistoryEventRecorder,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(event: HistoryEvent): Result<Unit, DatabaseError> =
        withContext(dispatchers.io) {
            recorder.record(event)
        }
}
