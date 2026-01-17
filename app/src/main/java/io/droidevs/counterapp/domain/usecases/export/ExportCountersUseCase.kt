package io.droidevs.counterapp.domain.usecases.export

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.RootError
import io.droidevs.counterapp.domain.result.flatMapSuspended
import io.droidevs.counterapp.domain.result.resultSuspend
import io.droidevs.counterapp.domain.services.ExportFormat
import io.droidevs.counterapp.domain.services.ExportSuccessResult
import io.droidevs.counterapp.domain.services.FileExportService
import io.droidevs.counterapp.domain.usecases.counters.GetAllCountersUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Deprecated("Use ExportUseCase instead")
class ExportCountersUseCase @Inject constructor(
    private val getAllCountersUseCase: GetAllCountersUseCase,
    private val fileExportService: FileExportService,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(format: ExportFormat): Result<ExportSuccessResult, RootError> =
        withContext(dispatchers.io) {
            resultSuspend {
                getAllCountersUseCase().combineSuspended { counters ->
                    fileExportService.export(counters, emptyList(), format)
                }.first()
            }
        }
}
