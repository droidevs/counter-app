package io.droidevs.counterapp.domain.usecases.category

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllCategoriesPagedUseCase @Inject constructor(
    private val repository: CategoryRepository,
    private val dispatchers: DispatcherProvider
) {
    operator fun invoke(pageNumber: Int, pageSize: Int): Flow<Result<List<Category>, DatabaseError>> =
        repository.allCategoriesPaged(pageNumber, pageSize).flowOn(dispatchers.io)
}

