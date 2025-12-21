package io.droidevs.counterapp.domain.usecases.category

import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.usecases.category.requests.DeleteCategoryRequest

class DeleteCategoryUseCase(private val repository: CategoryRepository) {
    operator fun invoke(request: DeleteCategoryRequest) {
        repository.deleteCategory(request.categoryId)
    }
}