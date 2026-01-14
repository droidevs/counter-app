package io.droidevs.counterapp.domain.usecases.importing

import android.net.Uri
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.services.FileImportService
import io.droidevs.counterapp.domain.services.ImportResult
import io.droidevs.counterapp.domain.services.ImportResult.*
import javax.inject.Inject

class ImportUseCase @Inject constructor(
    private val fileImportService: FileImportService,
    private val counterRepository: CounterRepository,
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(fileUri: Uri): ImportResult<Unit> {
        return when (val result = fileImportService.import(fileUri)) {
            is ImportResult.Success -> {
                try {
                    categoryRepository.importCategories(result.data.categories)
                    counterRepository.importCounters(result.data.counters)
                    Success(Unit)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Error("Failed to save backup: ${e.message}")
                }
            }
            is ImportResult.Error -> result
            is ImportResult.Cancelled -> result
        }
    }
}
