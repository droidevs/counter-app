package io.droidevs.counterapp.domain.repository

import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.model.CounterWithCategory
import kotlinx.coroutines.flow.Flow

interface CounterRepository {


    fun getCounter(id: String): Flow<Counter?>
    fun getAllCounters(): Flow<List<Counter>>

    fun getLastEdited(limit: Int) : Flow<List<Counter>>

    fun getTotalCounters() : Flow<Int>

    suspend fun saveCounter(counter : Counter)

    suspend fun createCounter(counter : Counter)

    suspend fun deleteCounter(counter : Counter)

    fun getCountersWithCategories(): Flow<List<CounterWithCategory>>

    fun getLastEditedWithCategory(limit : Int) : Flow<List<CounterWithCategory>>

    suspend fun seedDefaults()

    fun getSystemCounters(): Flow<List<Counter>>

    suspend fun incrementSystemCounter(
        counterKey: String
    )

    suspend fun updateSystemCounter(
        counterKey: String,
        count: Int
    )

}