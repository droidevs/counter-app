package io.droidevs.counterapp.ui.vm.events

import io.droidevs.counterapp.ui.CounterSnapshotParcelable

sealed class HomeEvent {
    data class NavigateToCounterView(val counter: CounterSnapshotParcelable) : HomeEvent()
    object NavigateToCreateCounter : HomeEvent()
    data class NavigateToCategoryView(val categoryId: String) : HomeEvent()
    object NavigateToCreateCategory : HomeEvent()
    object NavigateToCounterList : HomeEvent()
    object NavigateToCategoryList : HomeEvent()
    data class ShowMessage(val message: String) : HomeEvent()
}
