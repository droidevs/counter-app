package io.droidevs.counterapp.ui.vm.events

sealed class CategoryListEvent {
    data class NavigateToCategoryView(val categoryId: String) : CategoryListEvent()
    object NavigateToCreateCategory : CategoryListEvent()
//    data class ShowMessage(val message: String) : CategoryListEvent()
}
