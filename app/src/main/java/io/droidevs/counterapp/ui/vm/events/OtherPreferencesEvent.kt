package io.droidevs.counterapp.ui.vm.events

import android.net.Uri
import io.droidevs.counterapp.domain.services.ExportFormat

sealed interface OtherPreferencesEvent {
    data object ShowRemoveConfirmation : OtherPreferencesEvent
    data object RemoveSuccess : OtherPreferencesEvent
    data class ShowExportFormatDialog(val formats: List<ExportFormat>) : OtherPreferencesEvent
    data class ShareExportFile(val fileUri: Uri) : OtherPreferencesEvent
    data class Error(val message: String) : OtherPreferencesEvent
}