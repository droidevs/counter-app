package io.droidevs.counterapp.domain.usecases.export

import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.services.ExportFormat
import io.droidevs.counterapp.domain.services.ExportResult
import io.droidevs.counterapp.domain.services.FileExportService
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ExportCountersUseCase @Inject constructor(
    private val counterRepository: CounterRepository,
    private val fileExportService: FileExportService
) {
    suspend operator fun invoke(format: ExportFormat): ExportResult {
        val counters = counterRepository.getAllCounters().first()
        return fileExportService.export(counters, format)
    }
}
