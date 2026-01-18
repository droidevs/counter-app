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
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvent(event: HistoryEventEntity)

    @Transaction
    @Query("SELECT * FROM history_events ORDER BY timestamp DESC")
    fun getAllEventsWithCounter(): Flow<List<HistoryEventWithCounter>>

    @Query("DELETE FROM history_events")
    suspend fun clearAllEvents()
}
