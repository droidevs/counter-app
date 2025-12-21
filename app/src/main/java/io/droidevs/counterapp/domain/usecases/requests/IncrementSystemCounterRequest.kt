package io.droidevs.counterapp.domain.usecases.requests


// Request to increment/decrement system counter
data class IncrementSystemCounterRequest(
    val counterKey: String,
//    val amount: Int = 1
) {
    companion object {
        fun of(counterKey: String) =
            IncrementSystemCounterRequest(counterKey)
    }
}