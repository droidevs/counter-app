package io.droidevs.counterapp.domain.usecases.requests

import java.time.Instant

// Request to update counter fields (partial update)
data class UpdateCounterRequest(
    val counterId: String,
    val newName: String? = null,
    val newCategoryId: String? = null,
    val newCount: Int? = null,
    val canIncrease: Boolean? = null,
    val canDecrease: Boolean? = null,

    // Per-counter behavior overrides (nullable = clear override)
    val incrementStep: Int? = null,
    val decrementStep: Int? = null,
    val minValue: Int? = null,
    val maxValue: Int? = null,
    val defaultValue: Int? = null,
    val useDefaultBehavior: Boolean? = null,

    val lastUpdatedAt: Instant? = null,
    val orderAnchorAt: Instant? = null
) {
    companion object {
        fun of(
            counterId: String,
            newName: String? = null,
            newCategoryId: String? = null,
            newCount: Int? = null,
            canDecrease: Boolean? = null,
            canIncrease: Boolean? = null,
            incrementStep: Int? = null,
            decrementStep: Int? = null,
            minValue: Int? = null,
            maxValue: Int? = null,
            defaultValue: Int? = null,
            useDefaultBehavior: Boolean? = null,
            lastUpdatedAt: Instant? = null,
            orderAnchorAt: Instant? = null
        ) = UpdateCounterRequest(
            counterId = counterId,
            newName = newName,
            newCategoryId = newCategoryId,
            newCount = newCount,
            canIncrease = canIncrease,
            canDecrease = canDecrease,
            incrementStep = incrementStep,
            decrementStep = decrementStep,
            minValue = minValue,
            maxValue = maxValue,
            defaultValue = defaultValue,
            useDefaultBehavior = useDefaultBehavior,
            lastUpdatedAt = lastUpdatedAt,
            orderAnchorAt = orderAnchorAt
        )
    }
}