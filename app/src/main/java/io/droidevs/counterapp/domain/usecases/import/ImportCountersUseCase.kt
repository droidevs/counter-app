package io.droidevs.counterapp.domain.usecases.import

import android.net.Uri
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.services.FileImportService
import io.droidevs.counterapp.domain.services.ImportResult
import javax.inject.Inject

@Deprecated("Use ImportUseCase instead")
class ImportCountersUseCase @Inject constructor(
    private val counterRepository: CounterRepository,
    private val fileImportService: FileImportService
) {
    suspend operator fun invoke(fileUri: Uri): ImportResult<Unit> {
        return when (val result = fileImportService.import(fileUri)) {
            is ImportResult.Success -> {
                counterRepository.importCounters(result.data.counters)
                ImportResult.Success(Unit)
            }
            is ImportResult.Error -> result
        }
    }
}
