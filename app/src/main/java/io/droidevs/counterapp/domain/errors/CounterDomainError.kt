package io.droidevs.counterapp.domain.errors

import io.droidevs.counterapp.domain.result.RootError

/** Base class for errors coming from counter domain operations (increment/decrement/reset). */
sealed class CounterDomainError : RootError {
    data class FailedToIncrement(val cause: Throwable? = null) : CounterDomainError()
    data object IncrementBlockedByMaximum : CounterDomainError()

    data class FailedToDecrement(val cause: Throwable? = null) : CounterDomainError()
    data object DecrementBlockedByMinimum : CounterDomainError()

    data class FailedToReset(val cause: Throwable? = null) : CounterDomainError()
}
