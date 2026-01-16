package io.droidevs.counterapp.domain.usecases.category

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetExistingCategoryColorsUseCase @Inject constructor(
    private val repository: CategoryRepository,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(): Result<List<Int>, DatabaseError> =
        withContext(dispatchers.io) {
            repository.getExistingCategoryColors()
        }
}
