package io.droidevs.counterapp.domain.usecases.category

import io.droidevs.counterapp.domain.model.CategoryWithCounters
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.usecases.category.requests.GetCategoryWithCountersRequest
import kotlinx.coroutines.flow.Flow

class GetCategoryWithCountersUseCase(private val repository: CategoryRepository) {
    operator fun invoke(request: GetCategoryWithCountersRequest): Flow<CategoryWithCounters> {
        return repository.categoryWithCounters(request.categoryId)
    }
}