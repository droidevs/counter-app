package io.droidevs.counterapp.domain.usecases.export

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.services.ExportFormat
import io.droidevs.counterapp.domain.services.FileExportService
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAvailableExportFormatsUseCase @Inject constructor(
    private val fileExportService: FileExportService,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(): List<ExportFormat> = withContext(dispatchers.io) {
        fileExportService.getAvailableExportFormats()
    }
}