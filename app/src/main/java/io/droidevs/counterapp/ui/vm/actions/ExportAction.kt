package io.droidevs.counterapp.ui.vm.actions

import io.droidevs.counterapp.domain.services.ExportFormat

sealed interface ExportAction {
    data object RequestExport : ExportAction
    data class Export(val format: ExportFormat) : ExportAction
}