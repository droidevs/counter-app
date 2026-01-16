package io.droidevs.counterapp.domain.usecases.category

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.usecases.category.requests.DeleteCategoryRequest
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(request: DeleteCategoryRequest) =
        withContext(dispatchers.io) {
            repository.deleteCategory(request.categoryId)
        }
}
