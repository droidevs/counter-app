package io.droidevs.counterapp.domain.usecases.backup

import android.net.Uri
import io.droidevs.counterapp.domain.backup.BackupStorage
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.RootError
import io.droidevs.counterapp.domain.result.resultSuspendFromFlow
import io.droidevs.counterapp.domain.services.ExportFormat
import io.droidevs.counterapp.domain.usecases.export.ExportUseCase
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
    suspend operator fun invoke(): Result<Uri, RootError> =
        resultSuspendFromFlow {
            // Get config (from prefs), then export, then persist.
            getBackupConfigUseCase()
                .combineSuspended { config ->
                    exportUseCase(format = ExportFormat.JSON, exportOnlyNonSystem = false)
                        .combineSuspended { exported ->
                            backupStorage.save(
                                exportedFileUri = exported.fileUri,
                                exportedFileName = exported.fileName,
                                destination = config.location
                            )
                        }
                }
        }
}
