package io.droidevs.counterapp.ui.vm.states

import io.droidevs.counterapp.ui.models.CategoryUiModel

data class CreateCounterUiState(
    val name: String = "",
    val canIncrease: Boolean = true,
    val canDecrease: Boolean = true,
    val initialValue: Int = 0,
    val isInitialValueInputVisible: Boolean = false,
    val categoryId: String? = null,
    val categories: List<CategoryUiModel> = emptyList(),

    // Per-counter behavior overrides (kept as text for user input)
    val useDefaultBehavior: Boolean = true,
    val incrementStepInput: String = "",
    val decrementStepInput: String = "",
    val defaultValueInput: String = "",
    val minValueInput: String = "",
    val maxValueInput: String = "",

    val isSaving: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
