package io.droidevs.counterapp.ui.vm.events

sealed interface ImportEvent {
    object ShowImportFileChooser : ImportEvent

    // data class ShowMessage(val message: String) : ImportEvent
}
