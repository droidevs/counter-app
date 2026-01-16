package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.repository.CounterRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoveAllCountersUseCase @Inject constructor(
    private val counterRepository: CounterRepository,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke() = withContext(dispatchers.io) {
        counterRepository.deleteAllCounters()
    }
}
