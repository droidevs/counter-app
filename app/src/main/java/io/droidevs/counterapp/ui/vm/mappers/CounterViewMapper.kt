package io.droidevs.counterapp.ui.vm.mappers

import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.vm.states.CounterViewUiState

fun CounterUiModel.toViewUiState(isLoading: Boolean = false): CounterViewUiState {
    return CounterViewUiState(
        counter = this,
        isLoading = isLoading
    )
}
