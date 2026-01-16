package io.droidevs.counterapp.domain.usecases.export

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.services.ExportFormat
import io.droidevs.counterapp.domain.services.ExportResult
import io.droidevs.counterapp.domain.services.FileExportService
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExportUseCase @Inject constructor(
    private val counterRepository: CounterRepository,
    private val categoryRepository: CategoryRepository,
    private val fileExportService: FileExportService,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(format: ExportFormat): ExportResult = withContext(dispatchers.io) {
        val counters = counterRepository.exportCounters()
        val categories = categoryRepository.exportCategories()
        fileExportService.export(counters, categories, format)
    }
}