package io.droidevs.counterapp.domain.usecases.importing

import android.net.Uri
import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.RootError
import io.droidevs.counterapp.domain.result.ResultBuilder
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import io.droidevs.counterapp.domain.result.errors.FileError
import io.droidevs.counterapp.domain.result.resultSuspend
import io.droidevs.counterapp.domain.services.FileImportService
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImportUseCase @Inject constructor(
    private val fileImportService: FileImportService,
    private val counterRepository: CounterRepository,
    private val categoryRepository: CategoryRepository,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(fileUri: Uri): Result<Unit, RootError> = withContext(dispatchers.io) {
        resultSuspend {
            // Import from file -> save categories -> save counters
            ResultBuilder<Unit, RootError>().combineSuspended(
                first = {
                    // Import file data and map service errors to FileError
                    fileImportService.import(fileUri)
                },
                block = { imported ->
                    // Persist categories
                    categoryRepository.importCategories(imported.categories)
                        .combineSuspended {
                            // Persist counters
                            counterRepository.importCounters(imported.counters)
                        }
                }
            )
        }
    }
}