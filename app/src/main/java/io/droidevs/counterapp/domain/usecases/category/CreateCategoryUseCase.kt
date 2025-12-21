package io.droidevs.counterapp.domain.usecases.category

import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.usecases.category.requests.CreateCategoryRequest

class CreateCategoryUseCase(private val repository: CategoryRepository) {
    suspend operator fun invoke(request: CreateCategoryRequest) {
        repository.createCategory(request.category)
    }
}