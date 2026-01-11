package io.droidevs.counterapp.domain.services

import android.net.Uri
import io.droidevs.counterapp.domain.model.Counter
import java.io.File

interface FileExportService {
    suspend fun exportCounters(counters: List<Counter>): ExportResult
    suspend fun exportToFile(counters: List<Counter>): Uri
    suspend fun exportToShare(counters: List<Counter>): ExportResult
    fun getExportDirectory(): File
    fun getAvailableExportFormats(): List<ExportFormat>
}