package io.droidevs.counterapp.ui.vm.mappers

import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.model.CategoryColor
import io.droidevs.counterapp.ui.vm.states.CreateCategoryUiState

fun Pair<String, Int>.toCreateCategoryUiState(colors: List<CategoryColor>, isSaving: Boolean): CreateCategoryUiState {
    return CreateCategoryUiState(
        name = this.first,
        selectedColor = this.second,
        colors = colors,
        isSaving = isSaving
    )
}
