package io.droidevs.counterapp.ui.vm.events

sealed interface ImportEvent {
    data class ShowImportFileChooser(val mimeTypes: Array<String>) : ImportEvent
}
