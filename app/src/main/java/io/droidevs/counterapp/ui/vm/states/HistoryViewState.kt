package io.droidevs.counterapp.ui.vm.states

import io.droidevs.counterapp.ui.models.HistoryUiModel

data class HistoryViewState(
    val history: List<HistoryUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
