package io.droidevs.counterapp.data.service

import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.FileError
import io.droidevs.counterapp.domain.result.runCatchingResult
import java.io.IOException

suspend fun <D> runCatchingFileResult(
    block: suspend () -> D
): Result<D, FileError> = runCatchingResult(
    errorTransform = { e ->
        when (e) {
            is IOException -> FileError.ReadError("Error reading or writing file", e)
            else -> FileError.UnknownError("An unknown file error occurred", e)
        }
    }
) {
    block()
}
