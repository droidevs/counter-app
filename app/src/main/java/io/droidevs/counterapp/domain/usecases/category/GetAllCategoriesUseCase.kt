package io.droidevs.counterapp.domain.usecases.category

import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

// Use case to get all categories
class GetAllCategoriesUseCase(private val repository: CategoryRepository) {
    operator fun invoke(): Flow<List<Category>> {
        return repository.allCategories()
    }
}
