package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import io.droidevs.counterapp.domain.usecases.requests.DeleteCounterRequest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteCounterUseCase @Inject constructor(
    private val repository: CounterRepository,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(request: DeleteCounterRequest): Result<Unit, DatabaseError> =
        withContext(dispatchers.io) {
            val counter = repository.getCounter(request.counterId)
                .firstOrNull()
            counter?.let { repository.deleteCounter(it) }
        }
}
