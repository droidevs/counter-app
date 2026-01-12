package io.droidevs.counterapp.ui.vm.events

sealed class HistoryViewEvent {
    data class ShowMessage(val message: String) : HistoryViewEvent()
}
