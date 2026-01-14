package io.droidevs.counterapp.ui.vm.actions

import android.net.Uri
import io.droidevs.counterapp.domain.services.ExportFormat

sealed class BackupPreferenceAction {
    data class SetAutoBackup(val enabled: Boolean) : BackupPreferenceAction()
    data class SetBackupInterval(val hours: Long) : BackupPreferenceAction()
    object TriggerManualExport : BackupPreferenceAction()
    data class Export(val format: ExportFormat) : BackupPreferenceAction()
    object TriggerManualImport : BackupPreferenceAction()
    data class Import(val fileUri: Uri) : BackupPreferenceAction()
}
