package io.droidevs.counterapp.data

import androidx.room.*

@Dao
interface CounterDao {


    @Insert
    suspend fun insert(counters: List<CounterEntity>)

    @Query("""
        SELECT * FROM counters
        ORDER BY last_updated_at DESC
        LIMIT :limit
    """)
    suspend fun getLastEdited(limit : Int) : List<CounterEntity>
}
