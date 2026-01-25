package io.droidevs.counterapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import io.droidevs.counterapp.data.entities.CounterEntity
import io.droidevs.counterapp.data.entities.CounterWithCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDao {

    @Query("SELECT * FROM counters WHERE id = :id")
    fun getCounter(id: String): Flow<CounterEntity?>

    /**
     * Deprecated: Use getCountersPaged instead.
     */
    @Deprecated("Use getCountersPaged(pageNumber, pageSize) for pagination support.")
    @Query("SELECT * FROM counters WHERE is_system = 0 ORDER BY last_updated_at DESC")
    fun getAll() : Flow<List<CounterEntity>>

    /**
     * Deprecated: Use getUserCountersPaged instead.
     */
    @Deprecated("Use getUserCountersPaged(pageNumber, pageSize) for pagination support.")
    @Query("SELECT * FROM counters WHERE is_system = 0 ORDER BY last_updated_at DESC")
    fun getAllUserCounters() : Flow<List<CounterEntity>>

    /**
     * New paginated method for user counters.
     */
    @Query("SELECT * FROM counters WHERE is_system = 0 ORDER BY last_updated_at DESC LIMIT :pageSize OFFSET (:pageNumber * :pageSize)")
    fun getUserCountersPaged(pageNumber: Int, pageSize: Int): Flow<List<CounterEntity>>

    /**
     * Deprecated: Use getSystemCountersPaged instead.
     */
    @Deprecated("Use getSystemCountersPaged(pageNumber, pageSize) for pagination support.")
    @Query("SELECT * FROM counters WHERE is_system = 1")
    fun getAllSystem(): Flow<List<CounterEntity>>

    /**
     * New paginated method for system counters.
     */
    @Query("SELECT * FROM counters WHERE is_system = 1 LIMIT :pageSize OFFSET (:pageNumber * :pageSize)")
    fun getSystemCountersPaged(pageNumber: Int, pageSize: Int): Flow<List<CounterEntity>>

    @Query("SELECT * FROM counters WHERE kay = :key LIMIT 1")
    suspend fun getByKey(key: String): CounterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(counters: List<CounterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(counter: CounterEntity)

    @Update
    suspend fun update(counter: CounterEntity) : Int

    @Insert
    suspend fun insert(counter: CounterEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(counters: List<CounterEntity>)

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

    @Query("DELETE FROM counters WHERE is_system = 0")
    suspend fun deleteAllUserCounters()

    @Query("DELETE FROM counters")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM counters WHERE is_system = 0 ORDER BY order_anchor_at DESC")
    fun getCountersWithCategories(): Flow<List<CounterWithCategoryEntity>>

    @Transaction
    @Query("SELECT * FROM counters WHERE is_system = 0 ORDER BY order_anchor_at DESC LIMIT :limit")
    fun getCountersWithCategories(limit : Int) : Flow<List<CounterWithCategoryEntity>>

    @Transaction
    @Query("SELECT * FROM counters WHERE is_system = 0 ORDER BY last_updated_at DESC LIMIT :limit")
    fun getLastEditedWithCategories(limit: Int): Flow<List<CounterWithCategoryEntity>>


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