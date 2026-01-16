package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import io.droidevs.counterapp.domain.usecases.requests.CreateCounterRequest
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateCounterUseCase @Inject constructor(
    private val repository: CounterRepository,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(request: CreateCounterRequest): Result<Unit, DatabaseError> =
        withContext(dispatchers.io) {
            repository.createCounter(request.counter)
        }
}
