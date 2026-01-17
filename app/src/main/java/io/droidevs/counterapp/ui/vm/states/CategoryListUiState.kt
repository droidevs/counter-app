package io.droidevs.counterapp.ui.vm.states

import io.droidevs.counterapp.ui.models.CategoryUiModel

data class CategoryListUiState(
    val categories: List<CategoryUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isSystem: Boolean = false
)
