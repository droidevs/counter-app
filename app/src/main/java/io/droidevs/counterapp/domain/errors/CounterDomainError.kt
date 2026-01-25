package io.droidevs.counterapp.domain.errors

import io.droidevs.counterapp.domain.result.RootError

/** Base class for errors coming from counter domain operations (increment/decrement/reset). */
sealed class CounterDomainError : DomainError {
    data object IncrementBlockedByMaximum : CounterDomainError()
    data object DecrementBlockedByMinimum : CounterDomainError()
}
