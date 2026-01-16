package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.usecases.requests.IncrementSystemCounterRequest
import kotlinx.coroutines.withContext
import javax.inject.Inject

class IncrementSystemCounterUseCase @Inject constructor(
    private val repository: CounterRepository,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(request: IncrementSystemCounterRequest) = withContext(dispatchers.io) {
        repository.incrementSystemCounter(request.counterKey)
    }
}
