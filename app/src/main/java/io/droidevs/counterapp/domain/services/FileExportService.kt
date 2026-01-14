package io.droidevs.counterapp.domain.services

import io.droidevs.counterapp.domain.model.Counter

interface FileExportService {
    suspend fun export(counters: List<Counter>, format: ExportFormat): ExportResult
    fun getAvailableExportFormats(): List<ExportFormat>
}
