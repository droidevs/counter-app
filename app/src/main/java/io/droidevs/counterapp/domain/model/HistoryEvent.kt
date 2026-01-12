package io.droidevs.counterapp.domain.model

import java.time.Instant

data class HistoryEvent(
    val id: Long,
    val counterId: String,
    val counterName: String,
    val oldValue: Int,
    val newValue: Int,
    val change: Int,
    val timestamp: Instant
)
