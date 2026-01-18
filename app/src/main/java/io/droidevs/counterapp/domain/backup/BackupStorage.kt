package io.droidevs.counterapp.domain.backup

import android.net.Uri
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.RootError
import io.droidevs.counterapp.domain.result.errors.Error

/**
 * Persists an exported backup file to the configured destination.
 *
 * The export itself is produced by the existing manual export pipeline.
 */
interface BackupStorage {
    suspend fun save(
        exportedFileUri: Uri,
        exportedFileName: String,
        destination: BackupLocation
    ): Result<Uri, BackupStorageError>
}

sealed class BackupStorageError : RootError {
    data class Unknown(val cause: Throwable) : BackupStorageError()
}