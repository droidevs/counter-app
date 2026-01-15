package io.droidevs.counterapp.ui.vm.events

sealed interface ImportEvent {
    object ShowImportFileChooser : ImportEvent
}
