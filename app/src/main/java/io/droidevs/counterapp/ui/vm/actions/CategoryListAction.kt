package io.droidevs.counterapp.ui.vm.actions

import io.droidevs.counterapp.ui.models.CategoryUiModel

sealed class CategoryListAction {
    data class CategoryClicked(val category: CategoryUiModel) : CategoryListAction()
    object CreateCategoryClicked : CategoryListAction()
    data class SetSystemMode(val isSystem: Boolean) : CategoryListAction()
}
