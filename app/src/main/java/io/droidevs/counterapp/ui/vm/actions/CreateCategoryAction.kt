package io.droidevs.counterapp.ui.vm.actions

sealed class CreateCategoryAction {
    data class NameChanged(val name: String) : CreateCategoryAction()
    data class ColorSelected(val colorInt: Int) : CreateCategoryAction()
    object CreateClicked : CreateCategoryAction()
    data class LoadPalette(val colors: List<io.droidevs.counterapp.domain.model.CategoryColor>) : CreateCategoryAction()
}
