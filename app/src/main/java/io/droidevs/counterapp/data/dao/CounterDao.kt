package io.droidevs.counterapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.droidevs.counterapp.data.entities.CounterEntity
import io.droidevs.counterapp.data.entities.CounterWithCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDao {


    @Query("SELECT * FROM counters WHERE is_system = 0")
    fun getAll() : Flow<List<CounterEntity>>

    @Query("SELECT * FROM counters WHERE is_system = 1")
    fun getAllSystem(): Flow<List<CounterEntity>>

    @Query("SELECT * FROM counters WHERE ```key``` = :key LIMIT 1")
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
        ORDER BY last_updated_at DESC
        LIMIT :limit
    """)
    fun getLastEdited(limit : Int) : Flow<List<CounterEntity>>

    @Query("SELECT COUNT(*) FROM counters WHERE is_system = 0")
    fun getTotalCounters(): Flow<Int>

    @Delete
    suspend fun delete(counter: CounterEntity)

    @Query("SELECT * FROM counters WHERE is_system = 0")
    fun getCountersWithCategories(): Flow<List<CounterWithCategoryEntity>>

    @Query("SELECT * FROM counters WHERE is_system = 0 ORDER BY last_updated_at DESC LIMIT :limit")
    fun getLastEditedCountersWithCategories(limit : Int) : Flow<List<CounterWithCategoryEntity>>
}