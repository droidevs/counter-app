package io.droidevs.counterapp.ui.vm.events

sealed class CreateCategoryEvent {
    data class CategoryCreated(val name: String) : CreateCategoryEvent()
    data class ShowMessage(val message: String) : CreateCategoryEvent()
    object NavigateBack : CreateCategoryEvent()
}
