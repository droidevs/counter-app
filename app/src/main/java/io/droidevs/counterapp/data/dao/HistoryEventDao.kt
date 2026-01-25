package io.droidevs.counterapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.droidevs.counterapp.data.entities.HistoryEventEntity
import io.droidevs.counterapp.data.entities.HistoryEventWithCounter
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryEventDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertEvent(event: HistoryEventEntity)

    /**
     * Inserts and returns the generated id.
     * Used by the merge recorder so it can reliably update the same row later.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertEventReturningId(event: HistoryEventEntity): Long

    /**
     * Deprecated: Use getAllEventsWithCounterPaged instead.
     */
    @Deprecated("Use getAllEventsWithCounterPaged(pageNumber, pageSize) for pagination support.")
    @Transaction
    @Query("SELECT * FROM history_events ORDER BY timestamp DESC")
    fun getAllEventsWithCounter(): Flow<List<HistoryEventWithCounter>>

    /**
     * New paginated method for history events.
     */
    @Transaction
    @Query("SELECT * FROM history_events ORDER BY timestamp DESC LIMIT :pageSize OFFSET (:pageNumber * :pageSize)")
    fun getAllEventsWithCounterPaged(pageNumber: Int, pageSize: Int): Flow<List<HistoryEventWithCounter>>

    @Query("DELETE FROM history_events")
    suspend fun clearAllEvents()

    /** Last event for a specific counter (for merge logic). */
    @Query("SELECT * FROM history_events WHERE counter_id = :counterId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastEventForCounter(counterId: String): HistoryEventEntity?

    /** Update a previously inserted event (for merge logic). */
    @Query(
        "UPDATE history_events SET old_value = :oldValue, new_value = :newValue, change = :change, timestamp = :timestamp WHERE id = :id"
    )
    suspend fun updateEvent(
        id: Long,
        oldValue: Int,
        newValue: Int,
        change: Int,
        timestamp: java.time.Instant
    )
}
