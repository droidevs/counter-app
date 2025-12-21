package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.model.CounterWithCategory
import io.droidevs.counterapp.domain.repository.CounterRepository
import kotlinx.coroutines.flow.Flow

class GetCountersWithCategoriesUseCase(private val repository: CounterRepository) {
    operator fun invoke(): Flow<List<CounterWithCategory>> = repository.getCountersWithCategories()
}