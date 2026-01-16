package io.droidevs.counterapp.domain.result.errors


sealed interface DataError : Error

sealed class DatabaseError(open val cause: Throwable? = null) : DataError {

    data object NotFound : DatabaseError()

    data class TimeOut(val timeOut: Long) : DatabaseError()

    data class QueryFailed(override val cause: Throwable) : DatabaseError(cause = cause)
    data class DatabaseLocked(val retryable: Boolean) : DatabaseError()

    // Constraint violations (e.g., unique key, NOT NULL)
    data class ConstraintFailed(
        override val cause: Throwable,
        val constraintType: ConstraintType
    ) : DatabaseError(cause) {
        enum class ConstraintType { UNIQUE, NOT_NULL, FOREIGN_KEY }
    }

    // Corrupted database file
    data class DatabaseCorrupted(
        override val cause: Throwable
    ) : DatabaseError(cause)

    // Timeouts or transient issues (retryable)
    data class TransientError(override val cause: Throwable) : DatabaseError(cause)

    // Other unexpected errors
    data class UnknownError(override val cause: Throwable) : DatabaseError(cause)
    class NoElementFound : DatabaseError()

}

sealed class PreferenceError : DataError {
    data class ReadError(val cause: Exception) : PreferenceError()
    data class WriteError(val cause: Exception) : PreferenceError()
    data object KeyNotFound : PreferenceError()
    data object IOError : PreferenceError()
    data object EmptyValue : PreferenceError()

    data class UnknownError(val cause: Throwable) : PreferenceError()
}