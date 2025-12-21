package io.droidevs.counterapp.domain.usecases.requests

// Request to update counter fields (partial update)
data class UpdateCounterRequest(
    val counterId: String,
    val newName: String? = null,
    val newCategoryId: String? = null,
    val newCount: Int? = null
) {
    companion object {
        fun of(counterId: String, newName: String? = null, newCategoryId: String? = null, newCount: Int? = null) =
            UpdateCounterRequest(counterId, newName, newCategoryId, newCount)
    }
}