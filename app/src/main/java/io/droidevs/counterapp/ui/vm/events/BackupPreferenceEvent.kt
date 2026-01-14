package io.droidevs.counterapp.ui.vm.events

import android.net.Uri
import io.droidevs.counterapp.domain.services.ExportFormat

sealed class BackupPreferenceEvent {
    data class ShowMessage(val message: String) : BackupPreferenceEvent()
    data class ShowExportFormatDialog(val formats: List<ExportFormat>) : BackupPreferenceEvent()
    data class ShareExportFile(val fileUri: Uri) : BackupPreferenceEvent()
    object ShowImportConfirmationDialog : BackupPreferenceEvent()
    object ShowImportFileChooser : BackupPreferenceEvent()
}
