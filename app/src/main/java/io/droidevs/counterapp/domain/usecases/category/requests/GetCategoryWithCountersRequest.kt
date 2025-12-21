package io.droidevs.counterapp.domain.usecases.category.requests

// Use case to get a category with its counters
data class GetCategoryWithCountersRequest(val categoryId: String) {
    companion object {
        fun of(categoryId: String) = GetCategoryWithCountersRequest(categoryId)
    }
}
