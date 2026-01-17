package io.droidevs.counterapp.ui.vm.states

import io.droidevs.counterapp.ui.models.CounterUiModel

data class CounterEditUiState(
    val counter: CounterUiModel? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isSaving: Boolean = false
)
