package io.droidevs.counterapp.domain.usecases.category

import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

// Use case to get top categories
class GetTopCategoriesUseCase(
    private val repository: CategoryRepository
) {
    operator fun invoke(limit: Int): Flow<List<Category>> {
        return repository.topCategories(limit)
    }
}