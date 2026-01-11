package io.droidevs.counterapp.ui.vm.events

sealed class HomeEvent {
    data class NavigateToCounterView(val counterId: String) : HomeEvent()
    object NavigateToCreateCounter : HomeEvent()
    data class NavigateToCategoryView(val categoryId: String) : HomeEvent()
    object NavigateToCreateCategory : HomeEvent()
    object NavigateToCounterList : HomeEvent()
    object NavigateToCategoryList : HomeEvent()
    data class ShowMessage(val message: String) : HomeEvent()
}
