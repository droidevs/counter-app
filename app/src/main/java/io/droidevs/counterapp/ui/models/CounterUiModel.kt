package io.droidevs.counterapp.ui.models


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

    // Per-counter optional behavior overrides (nullable)
    val incrementStep: Int? = null,
    val decrementStep: Int? = null,
    val minValue: Int? = null,
    val maxValue: Int? = null,
    val defaultValue: Int? = null,
    val useDefaultBehavior: Boolean = true,

    // Formatted display strings (UI)
    val createdTime: String? = null,
    val editedTime: String? = null,

    // Derived flags
    val wasUserUpdated: Boolean = false
)