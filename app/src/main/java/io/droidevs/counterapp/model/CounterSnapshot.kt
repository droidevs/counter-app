package io.droidevs.counterapp.model

import java.time.Instant

data class CounterSnapshot(
    val id: String,
    val name: String,
    val currentCount: Int,
    val maxCount: Int?,
    val createdAt: Instant,
    val lastUpdatedAt: Instant
)
