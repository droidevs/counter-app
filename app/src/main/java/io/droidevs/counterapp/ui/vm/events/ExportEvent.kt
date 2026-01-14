package io.droidevs.counterapp.ui.vm.events

import android.net.Uri
import io.droidevs.counterapp.domain.services.ExportFormat

sealed interface ExportEvent {
    data class ShowExportFormatDialog(val formats: List<ExportFormat>) : ExportEvent
    data class ShareExportFile(val fileUri: Uri) : ExportEvent
    data class ShowMessage(val message: String) : ExportEvent
}