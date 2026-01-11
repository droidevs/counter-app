package io.droidevs.counterapp.ui.vm.events

import io.droidevs.counterapp.ui.CounterSnapshotParcelable

sealed class CounterListEvent {
    data class NavigateToCounterView(val counter: CounterSnapshotParcelable) : CounterListEvent()
    object NavigateToCreateCounter : CounterListEvent()
    data class ShowMessage(val message: String) : CounterListEvent()
}
