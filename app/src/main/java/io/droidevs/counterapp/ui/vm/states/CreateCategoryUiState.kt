package io.droidevs.counterapp.ui.vm.states

import io.droidevs.counterapp.domain.model.CategoryColor

data class CreateCategoryUiState(
    val name: String = "",
    val selectedColor: Int = 0,
    val colors: List<CategoryColor> = emptyList(),
    val isSaving: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
