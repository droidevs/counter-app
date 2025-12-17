package io.droidevs.counterapp.domain.repository

import io.droidevs.counterapp.domain.model.Counter
import kotlinx.coroutines.flow.Flow

interface CounterRepository {

    fun getAllCounters(): Flow<List<Counter>>

    fun getTotalCounters() : Flow<Int>

    suspend fun saveCounter(counter : Counter)

}