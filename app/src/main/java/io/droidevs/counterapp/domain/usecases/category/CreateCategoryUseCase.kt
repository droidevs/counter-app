package io.droidevs.counterapp.domain.usecases.category

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.usecases.category.requests.CreateCategoryRequest
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository,
    private val dispatchers: DispatcherProvider
) {

    suspend operator fun invoke(request: CreateCategoryRequest) =
        withContext(dispatchers.io) {
            repository.createCategory(request.category)
        }
}