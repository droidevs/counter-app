package io.droidevs.counterapp.domain.services

import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.model.Counter

interface FileExportService {
    suspend fun export(counters: List<Counter>, categories: List<Category>, format: ExportFormat): ExportResult
    fun getAvailableExportFormats(): List<ExportFormat>
}
