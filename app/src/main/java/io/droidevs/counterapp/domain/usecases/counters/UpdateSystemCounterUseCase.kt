package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.usecases.requests.UpdateSystemCounterRequest
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateSystemCounterUseCase @Inject constructor(
    private val repository: CounterRepository,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(request: UpdateSystemCounterRequest) = withContext(dispatchers.io) {
        repository.updateSystemCounter(request.counterKey, request.count)
    }
}
