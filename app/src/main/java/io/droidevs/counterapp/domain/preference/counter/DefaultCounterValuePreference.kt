package io.droidevs.counterapp.domain.preference.counter

import kotlinx.coroutines.flow.Flow

interface DefaultCounterValuePreference {
    fun get(): Flow<Int>
    suspend fun set(value: Int)
}