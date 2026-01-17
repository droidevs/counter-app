package io.droidevs.counterapp.domain.services

import java.time.Instant

data class CounterExport(
    val id: String,
    val name: String,
    val value: Int,
    val category: String? = null,
    val createdAt: Instant,
    val updatedAt: Instant? = null,
    val orderAnchorAt: Instant? = null,
    val canIncrease: Boolean = true,
    val canDecrease: Boolean = false,
    val isSystem: Boolean = false
)