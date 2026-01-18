package io.droidevs.counterapp.data.service

import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.FileError
import io.droidevs.counterapp.domain.result.runCatchingResult
import java.io.FileNotFoundException
import java.io.IOException

suspend fun <D> runCatchingFileResult(
    block: suspend () -> D
): Result<D, FileError> = runCatchingResult(
    errorTransform = { e ->
        when (e) {
            // Storage / permission related
            is SecurityException -> FileError.ReadError("Missing permission to access the selected file", e)
            is FileNotFoundException -> FileError.ReadError("Selected file was not found", e)

            // IO (can happen on both read and write)
            is IOException -> {
                val msg = e.message.orEmpty().lowercase()
                val looksLikeWrite = msg.contains("write") || msg.contains("disk") || msg.contains("space")
                if (looksLikeWrite) FileError.WriteError("Error writing file", e)
                else FileError.ReadError("Error reading file", e)
            }

            // Used by importer when the file is readable but schema/format isn't supported
            is IllegalArgumentException -> FileError.UnsupportedFormat(
                message = e.message ?: "Unsupported file format",
                detectedFormat = null
            )

            else -> FileError.UnknownError("An unknown file error occurred", e)
        }
    }
) {
    block()
}
