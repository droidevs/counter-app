package io.droidevs.counterapp.util

import io.droidevs.counterapp.domain.result.RootError
import io.droidevs.counterapp.domain.result.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Small injectable helper that runs blocks inside Sentry child spans.
 * Use by injecting into ViewModels and delegating coroutine launches to it.
 */
class TracingHelper {

    /**
     * Run a suspending block inside a child span and return the result.
     */
    suspend fun <T> tracedSuspend(spanName: String, block: suspend () -> T): T {
        return SentryTrace.withSpanSuspend(spanName) {
            block()
        }
    }

    /**
     * Run a suspending block that returns Result and set span status according to the Result.
     */
    suspend fun <D, E : RootError> tracedSuspendResult(
        spanName: String,
        block: suspend () -> Result<D, E>
    ): Result<D, E> {
        return SentryTrace.withSpanSuspend(spanName, block)
    }
}
