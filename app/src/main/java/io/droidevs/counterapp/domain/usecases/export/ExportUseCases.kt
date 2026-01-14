package io.droidevs.counterapp.domain.usecases.export

import javax.inject.Inject


data class ExportUseCases @Inject constructor(
    val export: ExportUseCase,
    val getAvailableExportFormats: GetAvailableExportFormatsUseCase
)