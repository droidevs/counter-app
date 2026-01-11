package io.droidevs.counterapp.ui.vm.events

sealed class CategoryViewEvent {
    object NavigateBack : CategoryViewEvent()
    data class NavigateToCreateCounter(val categoryId: String) : CategoryViewEvent()
    data class ShowMessage(val message: String) : CategoryViewEvent()
}
