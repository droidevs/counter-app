package io.droidevs.counterapp.ui.vm.actions

sealed class CategoryViewAction {
    object AddCounterClicked : CategoryViewAction()
    object DeleteCategoryClicked : CategoryViewAction()
    data class SetCategoryId(val categoryId: String) : CategoryViewAction()
}
