package io.droidevs.counterapp.domain.usecases.importing

import android.net.Uri
import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.services.FileImportService
import io.droidevs.counterapp.domain.services.ImportResult
import io.droidevs.counterapp.domain.services.ImportResult.Error
import io.droidevs.counterapp.domain.services.ImportResult.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Deprecated("Use ImportUseCase instead")
class ImportCountersUseCase @Inject constructor(
    private val counterRepository: CounterRepository,
    private val fileImportService: FileImportService,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(fileUri: Uri): ImportResult<Unit> = withContext(dispatchers.io) {
        when (val result = fileImportService.import(fileUri)) {
            is Result.Success -> {
                when (counterRepository.importCounters(result.data.counters)) {
                    is Result.Success -> Success(Unit)
                    is Result.Failure -> Error("Failed to import counters")
                }
            }

            is Result.Failure -> Error("Failed to import file")
        }
    }
}