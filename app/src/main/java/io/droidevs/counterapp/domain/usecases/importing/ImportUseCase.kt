package io.droidevs.counterapp.domain.usecases.importing

import android.net.Uri
import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.RootError
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
            fileImportService.import(fileUri)
                .combineSuspended { imported ->
                    // Persist categories first to satisfy potential FK relations
                    categoryRepository.importCategories(imported.categories)
                        .combineSuspended {
                            counterRepository.importCounters(imported.counters)
                        }
                }
        }
    }
}