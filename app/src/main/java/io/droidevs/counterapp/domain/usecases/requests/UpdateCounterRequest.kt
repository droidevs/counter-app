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
            lastUpdatedAt: Instant? = null,
            orderAnchorAt: Instant? = null
        ) = UpdateCounterRequest(counterId, newName, newCategoryId, newCount, canIncrease, canDecrease, lastUpdatedAt, orderAnchorAt)
    }
}