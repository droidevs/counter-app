package io.droidevs.counterapp.ui.vm.mappers

import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.vm.states.CounterEditUiState

fun CounterUiModel.toEditUiState(
    isLoading: Boolean = false,
    isSaving: Boolean = false,
    isError: Boolean = false
): CounterEditUiState {
    return CounterEditUiState(
        counter = this,
        isLoading = isLoading,
        isError = isError,
        isSaving = isSaving
    )
}
