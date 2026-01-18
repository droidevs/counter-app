package io.droidevs.counterapp.domain.usecases.backup

import android.net.Uri
import io.droidevs.counterapp.domain.backup.BackupStorage
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.RootError
import io.droidevs.counterapp.domain.result.errors.UnknownError
import io.droidevs.counterapp.domain.services.ExportFormat
import io.droidevs.counterapp.domain.usecases.export.ExportUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Executes auto-backup by reusing the existing manual export pipeline.
 *
 * 1) Export counters+categories to a temp file.
 * 2) Persist that file to the configured destination.
 */
class RunAutoBackupUseCase @Inject constructor(
    private val exportUseCase: ExportUseCase,
    private val getBackupConfigUseCase: GetBackupConfigUseCase,
    private val backupStorage: BackupStorage
) {
    suspend operator fun invoke(): Result<Uri, RootError> {
        val config = when (val cfg = getBackupConfigUseCase().first()) {
            is Result.Success -> cfg.data
            is Result.Failure -> return Result.Failure(UnknownError("Failed to read backup config"))
        }

        // For dev, keep it stable + small: always export JSON.
        val export = exportUseCase(format = ExportFormat.JSON, exportOnlyNonSystem = false)
        return when (export) {
            is Result.Success -> {
                val success = export.data
                when (val saved = backupStorage.save(success.fileUri, success.fileName, config.location)) {
                    is Result.Success -> Result.Success(saved.data)
                    is Result.Failure -> Result.Failure(UnknownError("Failed to save backup"))
                }
            }
            is Result.Failure -> Result.Failure(export.error)
        }
    }
}
