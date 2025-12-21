package io.droidevs.counterapp.domain.usecases.category

import io.droidevs.counterapp.domain.repository.CategoryRepository

// Get existing colors
class GetExistingCategoryColorsUseCase(private val repository: CategoryRepository) {
    suspend operator fun invoke(): List<Int> {
        return repository.getExistingCategoryColors()
    }
}