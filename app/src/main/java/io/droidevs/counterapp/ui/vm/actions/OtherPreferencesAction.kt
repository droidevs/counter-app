package io.droidevs.counterapp.ui.vm.actions

import io.droidevs.counterapp.domain.services.ExportFormat

sealed interface OtherPreferencesAction {
    data object RemoveCounters : OtherPreferencesAction
    data object ExportRequest : OtherPreferencesAction
    data class Export(val format: ExportFormat) : OtherPreferencesAction
    data object ConfirmRemoveCounters : OtherPreferencesAction
}