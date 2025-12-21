package io.droidevs.counterapp.domain.usecases.requests

import io.droidevs.counterapp.domain.model.Counter

// Request to create counter
data class CreateCounterRequest(val counter: Counter) {
    companion object {
        fun of(counter: Counter) = CreateCounterRequest(counter)
        fun of(name: String, categoryId: String?, count: Int = 0) =
            CreateCounterRequest(
                Counter(
                    id = java.util.UUID.randomUUID().toString(),
                    name = name,
                    categoryId = categoryId,
                    currentCount = count
                )
            )
    }
}