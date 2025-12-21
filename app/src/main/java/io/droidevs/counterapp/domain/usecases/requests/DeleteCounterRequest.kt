package io.droidevs.counterapp.domain.usecases.requests

import io.droidevs.counterapp.domain.model.Counter

// Request to delete counter
data class DeleteCounterRequest(val counterId: String) {
    companion object {
        fun of(counter: Counter) = DeleteCounterRequest(counter.id)
        fun of(counterId: String) = DeleteCounterRequest(counterId)
    }
}