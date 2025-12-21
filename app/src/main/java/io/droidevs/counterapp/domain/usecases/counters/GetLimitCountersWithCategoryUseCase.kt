package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.model.CounterWithCategory
import io.droidevs.counterapp.domain.repository.CounterRepository
import kotlinx.coroutines.flow.Flow

class GetLimitCountersWithCategoryUseCase(private val repository: CounterRepository) {
    operator fun invoke(limit: Int): Flow<List<CounterWithCategory>> = repository.getLastEditedWithCategory(limit)
}