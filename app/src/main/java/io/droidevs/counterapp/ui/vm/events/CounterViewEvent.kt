package io.droidevs.counterapp.ui.vm.events

import io.droidevs.counterapp.ui.CounterSnapshotParcelable

sealed class CounterViewEvent {
    data class NavigateToCounterEdit(val counter: CounterSnapshotParcelable) : CounterViewEvent()
    object NavigateBack : CounterViewEvent()
    data class ShowMessage(val message: String) : CounterViewEvent()
}
