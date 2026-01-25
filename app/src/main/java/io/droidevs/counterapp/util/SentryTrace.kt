package io.droidevs.counterapp.util

import io.sentry.Sentry
import io.sentry.SpanStatus
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.errors.DomainError
import io.droidevs.counterapp.domain.result.RootError

object SentryTrace {
    /**
     * Run a synchronous block inside a child span if a parent span exists.
     * Exceptions are recorded on the child span and rethrown.
     */
    fun <T> withSpan(name: String, block: () -> T): T {
        val parent = Sentry.getSpan()
        val child = parent?.startChild(name)
        try {
            val data = block()
            if (data is Result<*, *>) {
                when (data) {
                    is Result.Success<*> -> try { child?.status = SpanStatus.OK } catch (_: Throwable) {}
                    is Result.Failure<*> -> {
                        val err = data.error
                        if (err is DomainError) {
                            try { child?.status = SpanStatus.OK } catch (_: Throwable) {}
                        } else {
                            try { child?.status = SpanStatus.INTERNAL_ERROR } catch (_: Throwable) {}
                        }
                    }
                }
            } else {
                try { child?.status = SpanStatus.OK } catch (_: Throwable) {}
            }
            return data
        } catch (e: Exception) {
            try { child?.throwable = e } catch (_: Throwable) {}
            try { child?.status = SpanStatus.INTERNAL_ERROR } catch (_: Throwable) {}
            throw e
        } finally {
            try { child?.finish() } catch (_: Throwable) {}
        }
    }


    /**
     * Run a suspending block inside a child span if a parent span exists.
     */
    suspend fun <T> withSpanSuspend(name: String, block: suspend () -> T): T {
        val parent = Sentry.getSpan()
        val child = parent?.startChild(name)
        try {
            val data = block()
            if (data is Result<*, *>) {
                when (data) {
                    is Result.Success<*> -> try { child?.status = SpanStatus.OK } catch (_: Throwable) {}
                    is Result.Failure<*> -> {
                        val err = data.error
                        if (err is DomainError) {
                            try { child?.status = SpanStatus.OK } catch (_: Throwable) {}
                        } else {
                            try { child?.status = SpanStatus.INTERNAL_ERROR } catch (_: Throwable) {}
                        }
                    }
                }
            } else {
                try { child?.status = SpanStatus.OK } catch (_: Throwable) {}
            }
            return block()
        } catch (e: Exception) {
            try { child?.throwable = e } catch (_: Throwable) {}
            try { child?.status = SpanStatus.INTERNAL_ERROR } catch (_: Throwable) {}
            throw e
        } finally {
            try { child?.finish() } catch (_: Throwable) {}
        }
    }
}
