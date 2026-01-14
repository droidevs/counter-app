package io.droidevs.counterapp.domain.services

import io.droidevs.counterapp.domain.model.Counter

sealed class ImportResult {
    data class Success(val counters: List<Counter>) : ImportResult()
    data class Error(val message: String) : ImportResult()
    object Cancelled : ImportResult()
}
