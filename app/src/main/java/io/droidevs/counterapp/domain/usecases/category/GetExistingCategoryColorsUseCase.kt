package io.droidevs.counterapp.domain.usecases.category

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.repository.CategoryRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetExistingCategoryColorsUseCase @Inject constructor(
    private val repository: CategoryRepository,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(): List<Int> =
        withContext(dispatchers.io) {
            repository.getExistingCategoryColors()
        }
}
