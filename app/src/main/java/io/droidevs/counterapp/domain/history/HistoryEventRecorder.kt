package io.droidevs.counterapp.domain.history

import io.droidevs.counterapp.domain.model.HistoryEvent
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError

/**
 * Single entry-point for recording counter change events.
 *
 * Implementation may insert a new row OR merge into the latest one (same counter)
 * depending on configuration.
 */
interface HistoryEventRecorder {
    suspend fun record(event: HistoryEvent): Result<Unit, DatabaseError>
}

