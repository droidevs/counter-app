package io.droidevs.counterapp.ui.vm.states

import io.droidevs.counterapp.ui.models.CounterUiModel

data class CounterViewUiState(
    val counter: CounterUiModel? = null,
    val isLoading: Boolean = false
)
