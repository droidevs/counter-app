package io.droidevs.counterapp.domain.usecases.importing

import android.net.Uri
import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.services.FileImportService
import io.droidevs.counterapp.domain.services.ImportResult
import io.droidevs.counterapp.domain.services.ImportResult.*
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
            is Success -> {
                counterRepository.importCounters(result.data.counters)
                Success(Unit)
            }
            is Error -> result
            is Cancelled -> result
        }
    }
}