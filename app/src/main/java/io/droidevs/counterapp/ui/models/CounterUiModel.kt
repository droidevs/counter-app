package io.droidevs.counterapp.ui.models

import java.time.Instant

data class CounterUiModel(
    val id: String,
    val name: String,
    val currentCount: Int,
    val categoryId: String? = null,
    // Flags copied from domain
    val canIncrease: Boolean,
    val canDecrease: Boolean,
    val isSystem: Boolean = false,
    val systemKey: String? = null,
    val createdTime: String? = null,
    val editedTime : String? = null
)