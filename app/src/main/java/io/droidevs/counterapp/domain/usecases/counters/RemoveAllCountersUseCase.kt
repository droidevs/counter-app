package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.repository.CounterRepository
import javax.inject.Inject

class RemoveAllCountersUseCase @Inject constructor(
    private val counterRepository: CounterRepository
) {
    suspend operator fun invoke() {
        counterRepository.deleteAllCounters()
    }
}