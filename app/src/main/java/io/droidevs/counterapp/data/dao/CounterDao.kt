package io.droidevs.counterapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import io.droidevs.counterapp.data.entities.CounterEntity
import io.droidevs.counterapp.data.entities.CounterWithCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDao {


    @Query("SELECT * FROM counters WHERE id = :id")
    fun getCounter(id: String): Flow<CounterEntity>

    @Query("SELECT * FROM counters WHERE is_system = 0 ORDER BY last_updated_at DESC")
    fun getAll() : Flow<List<CounterEntity>>

    @Query("SELECT * FROM counters WHERE is_system = 1")
    fun getAllSystem(): Flow<List<CounterEntity>>

    @Query("SELECT * FROM counters WHERE kay = :key LIMIT 1")
    suspend fun getByKey(key: String): CounterEntity?

    @Insert
    suspend fun insertAll(counters: List<CounterEntity>)

    @Update
    suspend fun update(counter: CounterEntity) : Int

    @Insert
    suspend fun insert(counter: CounterEntity)

    @Query("""
        SELECT * FROM counters
        WHERE is_system = 0
        ORDER BY order_anchor_at DESC
        LIMIT :limit
    """)
    fun getCounters(limit : Int) : Flow<List<CounterEntity>>

    @Query("SELECT COUNT(*) FROM counters WHERE is_system = 0")
    fun getTotalCounters(): Flow<Int>

    @Delete
    suspend fun delete(counter: CounterEntity)

    @Query("DELETE FROM counters")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM counters WHERE is_system = 0 ORDER BY order_anchor_at DESC")
    fun getCountersWithCategories(): Flow<List<CounterWithCategoryEntity>>

    @Transaction
    @Query("SELECT * FROM counters WHERE is_system = 0 ORDER BY last_updated_at DESC LIMIT :limit")
    fun getCountersWithCategories(limit : Int) : Flow<List<CounterWithCategoryEntity>>


    @Query("""
        UPDATE counters
        SET current_count = current_count + 1
        WHERE kay = :counterKey
    """)
    suspend fun incrementSystemCounter(
        counterKey: String
    )

    @Query("""
        UPDATE counters
        SET current_count = :count
        WHERE kay = :counterKey
    """)
    suspend fun updateSystemCounter(
        counterKey: String,
        count: Int
    )
}