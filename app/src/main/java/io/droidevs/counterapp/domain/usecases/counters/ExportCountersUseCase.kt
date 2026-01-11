package io.droidevs.counterapp.domain.usecases.counters

import android.net.Uri
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.services.FileExportService
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class ExportCountersUseCase @Inject constructor(
    private val counterRepository: CounterRepository,
    private val fileExportService: FileExportService
) {
    suspend operator fun invoke(): Uri {
        val counters = counterRepository.getAllCounters().firstOrNull()
            ?: emptyList()
        return fileExportService.exportToFile(counters)
    }
}