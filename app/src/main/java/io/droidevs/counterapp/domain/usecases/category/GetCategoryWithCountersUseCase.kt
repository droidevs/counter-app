package io.droidevs.counterapp.domain.usecases.category

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.model.CategoryWithCounters
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.usecases.category.requests.GetCategoryWithCountersRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCategoryWithCountersUseCase @Inject constructor(
    private val repository: CategoryRepository,
    private val dispatchers: DispatcherProvider
) {
    operator fun invoke(request: GetCategoryWithCountersRequest): Flow<CategoryWithCounters> {
        return repository.categoryWithCounters(request.categoryId).flowOn(dispatchers.io)
    }
}