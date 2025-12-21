package io.droidevs.counterapp.domain.usecases.category

import io.droidevs.counterapp.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

// Use case to get total count of categories
class GetTotalCategoriesCountUseCase(private val repository: CategoryRepository) {
    operator fun invoke(): Flow<Int> {
        return repository.getTotalCategoriesCount()
    }
}