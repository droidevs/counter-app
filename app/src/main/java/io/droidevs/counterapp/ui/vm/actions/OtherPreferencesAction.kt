package io.droidevs.counterapp.ui.vm.actions

sealed interface OtherPreferencesAction {
    data object RemoveCounters : OtherPreferencesAction
    data object ExportCounters : OtherPreferencesAction
    data object ConfirmRemoveCounters : OtherPreferencesAction
}