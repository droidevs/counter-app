package io.droidevs.counterapp.domain.usecases.export

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.RootError
import io.droidevs.counterapp.domain.result.resultSuspendFromFlow
import io.droidevs.counterapp.domain.services.ExportFormat
import io.droidevs.counterapp.domain.services.ExportSuccessResult
import io.droidevs.counterapp.domain.services.FileExportService
import io.droidevs.counterapp.domain.usecases.category.GetAllCategoriesUseCase
import io.droidevs.counterapp.domain.usecases.counters.GetAllCountersUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExportUseCase @Inject constructor(
    private val getAllCountersUseCase: GetAllCountersUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val fileExportService: FileExportService,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(format: ExportFormat): Result<ExportSuccessResult, RootError> =
        withContext(dispatchers.io) {
            resultSuspendFromFlow {
                getAllCountersUseCase().combineFlow { counters ->
                    getAllCategoriesUseCase().combineSuspended { categories ->
                        fileExportService.export(counters, categories, format)
                    }
                }
            }
        }
}
