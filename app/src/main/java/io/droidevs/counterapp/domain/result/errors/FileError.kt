package io.droidevs.counterapp.domain.result.errors

sealed class FileError : RootError() {
    data class ReadError(override val message: String, val throwable: Throwable? = null) : FileError()
    data class WriteError(override val message: String, val throwable: Throwable? = null) : FileError()
    data class ShareError(override val message: String, val throwable: Throwable? = null) : FileError()
}
