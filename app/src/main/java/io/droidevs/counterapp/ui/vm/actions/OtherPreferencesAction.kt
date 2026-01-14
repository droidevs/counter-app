package io.droidevs.counterapp.ui.vm.actions

sealed class OtherPreferencesAction {
    object RemoveCounters : OtherPreferencesAction()
    object ConfirmRemoveCounters : OtherPreferencesAction()
}
