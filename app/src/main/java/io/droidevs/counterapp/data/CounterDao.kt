package io.droidevs.counterapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDao {


    @Query("SELECT * FROM counters")
    fun getAll() : Flow<List<CounterEntity>>
    @Insert
    suspend fun insertAll(counters: List<CounterEntity>)

    @Update
    suspend fun update(counter: CounterEntity) : Int

    @Insert
    suspend fun insert(counter: CounterEntity)

    @Query("""
        SELECT * FROM counters
        ORDER BY last_updated_at DESC
        LIMIT :limit
    """)
    fun getLastEdited(limit : Int) : Flow<List<CounterEntity>>

    @Query("SELECT COUNT(*) FROM counters")
    fun getTotalCounters(): Flow<Int>
}
