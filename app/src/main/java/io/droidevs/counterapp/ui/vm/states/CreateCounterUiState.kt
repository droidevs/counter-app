package io.droidevs.counterapp.ui.vm.states

import io.droidevs.counterapp.ui.models.CategoryUiModel

data class CreateCounterUiState(
    val name: String = "",
    val canIncrease: Boolean = true,
    val canDecrease: Boolean = true,
    val initialValue: Int = 0,
    val isInitialValueInputVisible: Boolean,
    val categoryId: String? = null,
    val categories: List<CategoryUiModel> = emptyList(),
    val isSaving: Boolean = false
)
