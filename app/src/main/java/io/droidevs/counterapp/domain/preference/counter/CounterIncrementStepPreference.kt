package io.droidevs.counterapp.domain.preference.counter

import kotlinx.coroutines.flow.Flow

interface CounterIncrementStepPreference {
    fun get(): Flow<Int>
    suspend fun set(value: Int)
}