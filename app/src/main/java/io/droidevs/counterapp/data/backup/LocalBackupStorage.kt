package io.droidevs.counterapp.data.backup

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import io.droidevs.counterapp.domain.backup.BackupLocation
import io.droidevs.counterapp.domain.backup.BackupStorage
import io.droidevs.counterapp.domain.backup.BackupStorageError
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.runCatchingResult
import java.io.File
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalBackupStorage @Inject constructor(
    @ApplicationContext private val context: Context
) : BackupStorage {

    override suspend fun save(
        exportedFileUri: Uri,
        exportedFileName: String,
        destination: BackupLocation
    ): Result<Uri, BackupStorageError> {
        return when (destination) {
            BackupLocation.Local -> saveToAppPrivateBackups(exportedFileUri, exportedFileName)
            BackupLocation.GoogleDrive -> Result.Failure(BackupStorageError.Unknown(IllegalStateException("Google Drive backup not supported yet")))
        }
    }

    private suspend fun saveToAppPrivateBackups(
        exportedFileUri: Uri,
        exportedFileName: String
    ): Result<Uri, BackupStorageError> = runCatchingResult(
        errorTransform = { e -> BackupStorageError.Unknown(e) }
    ) {
        val backupsDir = File(context.filesDir, "backups").apply { mkdirs() }

        val stamp = Instant.now().toString().replace(":", "-")
        val targetName = "auto_${stamp}_$exportedFileName"
        val targetFile = File(backupsDir, targetName)

        context.contentResolver.openInputStream(exportedFileUri).use { input ->
            requireNotNull(input) { "Unable to open exported file stream" }
            targetFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        // Return a file:// Uri for internal app usage (not for sharing).
        Uri.fromFile(targetFile)
    }
}

