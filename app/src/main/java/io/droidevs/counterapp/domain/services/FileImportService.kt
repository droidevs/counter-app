package io.droidevs.counterapp.domain.services

import android.net.Uri
import io.droidevs.counterapp.domain.model.Backup
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.FileError

interface FileImportService {
    suspend fun import(fileUri: Uri): Result<Backup, FileError>

    /** Formats that this app can successfully parse on import. */
    fun getAvailableImportFormats(): List<ExportFormat>
}
