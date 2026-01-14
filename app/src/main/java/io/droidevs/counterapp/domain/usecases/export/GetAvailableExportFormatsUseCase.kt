package io.droidevs.counterapp.domain.usecases.export

import io.droidevs.counterapp.domain.services.ExportFormat
import io.droidevs.counterapp.domain.services.FileExportService
import javax.inject.Inject

class GetAvailableExportFormatsUseCase @Inject constructor(
    private val fileExportService: FileExportService
) {
    operator fun invoke(): List<ExportFormat> {
        return fileExportService.getAvailableExportFormats()
    }
}
