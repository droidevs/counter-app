package io.droidevs.counterapp.data.preference.impl.exceptions


import io.droidevs.counterapp.domain.result.errors.PreferenceError
import io.droidevs.counterapp.domain.result.flowRunCatching
import io.droidevs.counterapp.domain.result.runCatchingResult
import io.droidevs.counterapp.domain.result.runCatchingWithResult
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import io.droidevs.counterapp.domain.result.Result


suspend fun <T> runCatchingPreference(
    block: suspend () -> T
) : Result<T, PreferenceError> = runCatchingResult(
    errorTransform = { e ->
        transformPreferenceError(e)
    }
) {
   block()
}

suspend fun <T> runCatchingPreferenceWithResult(
    block: suspend () -> Result<T, PreferenceError>
) : Result<T, PreferenceError> = runCatchingWithResult(
    errorTransform = { e ->
        transformPreferenceError(e)
    }
) {
    block()
}

fun <T> flowCatchingPreference(
    block: suspend () -> Flow<T>
) = flowRunCatching(
    errorTransform = { e->
        transformPreferenceError(e)
    }
){
    block()
}

private fun transformPreferenceError(e: Throwable): PreferenceError = when (e) {
    is NoSuchElementException -> PreferenceError.KeyNotFound
    is IOException -> PreferenceError.IOError
    else -> PreferenceError.UnknownError(e)
}