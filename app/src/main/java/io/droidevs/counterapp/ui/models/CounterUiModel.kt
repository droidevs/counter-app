package io.droidevs.counterapp.ui.models

import java.time.Instant

data class CounterUiModel(
    val id: String,
    val name: String,
    val currentCount: Int,
    val categoryId: String? = null,
    val createdAt: Instant,
    val lastUpdatedAt: Instant,
    val orderAnchorAt: Instant,
    // Flags copied from domain
    val canIncrease: Boolean,
    val canDecrease: Boolean
)