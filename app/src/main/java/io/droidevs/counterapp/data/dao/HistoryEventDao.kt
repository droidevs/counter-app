package io.droidevs.counterapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.droidevs.counterapp.data.entities.HistoryEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryEventDao {
    @Insert
    suspend fun insertEvent(event: HistoryEventEntity)

    @Query("SELECT * FROM history_events ORDER BY timestamp DESC")
    fun getAllEvents(): Flow<List<HistoryEventEntity>>

    @Query("DELETE FROM history_events")
    suspend fun clearAllEvents()
}
