package io.droidevs.counterapp.domain.usecases.history

data class HistoryUseCases(
    val getHistoryUseCase: GetHistoryUseCase,
    val addHistoryEventUseCase: AddHistoryEventUseCase,
    val clearHistoryUseCase: ClearHistoryUseCase
)
