package io.droidevs.counterapp.ui.models

import java.time.Instant

data class CounterSnapshot(
    val id: String,
    val name: String,
    val currentCount: Int,
    val categoryId: String? = null,
    val createdAt: Instant,
    val lastUpdatedAt: Instant,

    // Flags copied from domain
    val canIncrease: Boolean,
    val canDecrease: Boolean
)