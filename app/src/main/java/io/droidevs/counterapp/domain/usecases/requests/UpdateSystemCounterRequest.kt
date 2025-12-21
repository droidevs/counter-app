package io.droidevs.counterapp.domain.usecases.requests

// Request to update system counter value
data class UpdateSystemCounterRequest(val counterKey: String, val count: Int) {
    companion object {
        fun of(counterKey: String, count: Int) = UpdateSystemCounterRequest(counterKey, count)
    }
}