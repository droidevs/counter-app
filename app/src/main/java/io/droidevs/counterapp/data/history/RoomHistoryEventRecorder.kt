package io.droidevs.counterapp.data.history

import androidx.room.withTransaction
import io.droidevs.counterapp.data.AppDatabase
import io.droidevs.counterapp.domain.history.HistoryEventRecorder
import io.droidevs.counterapp.domain.history.HistoryMergeConfig
import io.droidevs.counterapp.domain.model.HistoryEvent
import io.droidevs.counterapp.domain.repository.HistoryRepository
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

class RoomHistoryEventRecorder @Inject constructor(
    private val db: AppDatabase,
    private val repository: HistoryRepository,
    private val config: HistoryMergeConfig
) : HistoryEventRecorder {

    override suspend fun record(event: HistoryEvent): Result<Unit, DatabaseError> {
        // Keep transactional behavior, but route all data ops through repository.
        return try {
            db.withTransaction {
                val lastResult = repository.getLastEventForCounter(event.counterId)

                when (lastResult) {
                    is Result.Failure -> lastResult
                    is Result.Success -> {
                        val last = lastResult.data
                        if (last != null && withinMergeWindow(last.timestamp, event.timestamp, config)) {
                            val mergedOld = last.oldValue
                            val mergedNew = event.newValue
                            val mergedChange = mergedNew - mergedOld

                            repository.updateHistoryEvent(
                                id = last.id,
                                oldValue = mergedOld,
                                newValue = mergedNew,
                                change = mergedChange,
                                timestamp = event.timestamp
                            )
                        } else {
                            repository.addHistoryEvent(event)
                        }
                    }
                }
            }
        } catch (t: Throwable) {
            // Convert unexpected errors to DatabaseError using repository helpers via a fake failure path
            // (RoomDatabase.withTransaction can throw).
            Result.Failure(DatabaseError.UnknownError(t))
        }
    }

    private fun withinMergeWindow(
        previousTimestamp: Instant,
        currentTimestamp: Instant,
        config: HistoryMergeConfig
    ): Boolean {
        val diff = Duration.between(previousTimestamp, currentTimestamp)
        val diffMillis = kotlin.math.abs(diff.toMillis())
        return diffMillis <= config.mergeWindow.inWholeMilliseconds
    }
}
