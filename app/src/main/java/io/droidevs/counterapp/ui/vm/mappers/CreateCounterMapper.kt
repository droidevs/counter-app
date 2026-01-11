package io.droidevs.counterapp.ui.vm.mappers

import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.vm.states.CreateCounterUiState

fun Triple<String, Boolean, Boolean>.toCreateCounterUiState(
    categoryId: String?,
    categories: List<CategoryUiModel> = emptyList(),
    isSaving: Boolean = false
): CreateCounterUiState {
    return CreateCounterUiState(
        name = this.first,
        canIncrease = this.second,
        canDecrease = this.third,
        categoryId = categoryId,
        categories = categories,
        isSaving = isSaving
    )
}
