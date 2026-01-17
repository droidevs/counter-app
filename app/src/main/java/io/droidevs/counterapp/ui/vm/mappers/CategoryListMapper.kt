package io.droidevs.counterapp.ui.vm.mappers

import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.vm.states.CategoryListUiState

fun List<CategoryUiModel>.toUiState(
    isLoading: Boolean,
    isError: Boolean,
    isSystem: Boolean
): CategoryListUiState {
    return CategoryListUiState(
        categories = this,
        isLoading = isLoading,
        isError = isError,
        isSystem = isSystem
    )
}
