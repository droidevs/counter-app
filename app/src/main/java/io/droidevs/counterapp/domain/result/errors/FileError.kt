package io.droidevs.counterapp.domain.result.errors

import io.droidevs.counterapp.domain.result.RootError

sealed class FileError : RootError {
    data class ReadError(val message: String, val throwable: Throwable? = null) : FileError()
    data class WriteError(val message: String, val throwable: Throwable? = null) : FileError()
    data class ShareError(val message: String, val throwable: Throwable? = null) : FileError()

    /** The file was readable, but the selected format/schema isn't supported by the current app version. */
    data class UnsupportedFormat(val message: String, val detectedFormat: String? = null) : FileError()

    data class UnknownError(val message: String, val throwable: Throwable? = null) : FileError()
}
