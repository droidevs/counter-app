package io.droidevs.counterapp.domain.repository

import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.model.CounterWithCategory
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import kotlinx.coroutines.flow.Flow

interface CounterRepository {


    fun getCounter(id: String): Flow<Result<Counter, DatabaseError>>
    fun getAllCounters(): Flow<Result<List<Counter>, DatabaseError>>

    fun getLastEdited(limit: Int) : Flow<Result<List<Counter>, DatabaseError>>

    fun getTotalCounters() : Flow<Result<Int, DatabaseError>>

    suspend fun saveCounter(counter : Counter): Result<Unit, DatabaseError>

    suspend fun createCounter(counter : Counter): Result<Unit, DatabaseError>

    suspend fun deleteCounter(counter : Counter): Result<Unit, DatabaseError>

    fun getCountersWithCategories(): Flow<Result<List<CounterWithCategory>, DatabaseError>>

    /** Same as getCountersWithCategories(), but limited and still sorted by orderAnchorAt. */
    fun getCountersWithCategories(limit: Int): Flow<Result<List<CounterWithCategory>, DatabaseError>>

    fun getLastEditedWithCategory(limit : Int) : Flow<Result<List<CounterWithCategory>, DatabaseError>>

    fun getSystemCounters(): Flow<Result<List<Counter>, DatabaseError>>

    suspend fun incrementSystemCounter(
        counterKey: String
    ): Result<Unit, DatabaseError>

    suspend fun updateSystemCounter(
        counterKey: String,
        count: Int
    ): Result<Unit, DatabaseError>

    suspend fun deleteAllCounters(): Result<Unit, DatabaseError>

    suspend fun exportCounters(): Result<List<Counter>, DatabaseError>

    suspend fun importCounters(counters: List<Counter>): Result<Unit, DatabaseError>

}
