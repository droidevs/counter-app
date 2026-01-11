package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.repository.CounterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCounterUseCase @Inject constructor(
    private val repository: CounterRepository
) {
    operator fun invoke(id: String): Flow<Counter?> {
        return repository.getCounter(id)
    }
}
