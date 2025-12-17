package io.droidevs.counterapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDao {


    @Insert
    suspend fun insertAll(counters: List<CounterEntity>)

    @Update
    suspend fun update(counter: CounterEntity) : Int

    @Query("""
        SELECT * FROM counters
        ORDER BY last_updated_at DESC
        LIMIT :limit
    """)
    fun getLastEdited(limit : Int) : Flow<List<CounterEntity>>

    @Query("SELECT COUNT(*) FROM counters")
    fun getTotalCounters(): Flow<Int>


}
