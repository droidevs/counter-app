package io.droidevs.counterapp.domain.preference.controle

import kotlinx.coroutines.flow.Flow

interface LabelControlPreference {
    fun get(): Flow<Boolean>
    suspend fun set(value: Boolean)
}