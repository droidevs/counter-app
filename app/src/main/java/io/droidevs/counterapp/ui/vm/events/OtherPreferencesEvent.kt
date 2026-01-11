package io.droidevs.counterapp.ui.vm.events

import android.net.Uri

sealed interface OtherPreferencesEvent {
    data object ShowRemoveConfirmation : OtherPreferencesEvent
    data object RemoveSuccess : OtherPreferencesEvent
    data class ExportSuccess(val fileUri: Uri) : OtherPreferencesEvent
    data class Error(val message: String) : OtherPreferencesEvent
}