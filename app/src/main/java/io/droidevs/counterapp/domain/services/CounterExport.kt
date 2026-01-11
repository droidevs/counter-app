package io.droidevs.counterapp.domain.services

import java.time.Instant

data class CounterExport(
    val id: String,
    val name: String,
    val value: Int,
    val category: String? = null,
    val createdAt: Instant,
    val updatedAt: Instant,
    val canIncrease: Boolean = true,
    val canDecrease: Boolean = false
)