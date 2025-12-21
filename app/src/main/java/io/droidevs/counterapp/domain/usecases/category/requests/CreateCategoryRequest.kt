package io.droidevs.counterapp.domain.usecases.category.requests

import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.model.CategoryColor
import java.util.UUID

// Create category
data class CreateCategoryRequest(val category: Category) {

    companion object {
        // Small, convenient factory method
        fun of(category: Category) = CreateCategoryRequest(category)

        // Optional: build from raw parameters
        fun of(name: String, color: Int) = CreateCategoryRequest(
            Category(
                id = UUID.randomUUID().toString(),
                name = name,
                color = CategoryColor.of(color),
                countersCount = 0,
                isSystem = false
            )
        )
    }
}
