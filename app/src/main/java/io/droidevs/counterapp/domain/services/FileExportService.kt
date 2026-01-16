package io.droidevs.counterapp.domain.services

import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.FileError

interface FileExportService {
    suspend fun export(counters: List<Counter>, categories: List<Category>, format: ExportFormat): Result<ExportSuccessResult, FileError>
    fun getAvailableExportFormats(): List<ExportFormat>
}
