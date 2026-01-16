package io.droidevs.counterapp.domain.usecases.export

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.services.ExportFormat
import io.droidevs.counterapp.domain.services.ExportResult
import io.droidevs.counterapp.domain.services.FileExportService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Deprecated("Use ExportUseCase instead")
class ExportCountersUseCase @Inject constructor(
    private val counterRepository: CounterRepository,
    private val fileExportService: FileExportService,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(format: ExportFormat): ExportResult = withContext(dispatchers.io) {
        val counters = counterRepository.getAllCounters().first()
        fileExportService.export(counters, emptyList(), format)
    }
}