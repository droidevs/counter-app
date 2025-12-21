package io.droidevs.counterapp.domain.usecases.category.requests

// Delete category
data class DeleteCategoryRequest(val categoryId: String) {

    companion object {
        fun of(categoryId: String) = DeleteCategoryRequest(categoryId)
    }

}