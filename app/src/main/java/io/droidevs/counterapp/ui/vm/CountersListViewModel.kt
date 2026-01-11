package io.droidevs.counterapp.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.vm.actions.CounterListAction
import io.droidevs.counterapp.ui.vm.events.CounterListEvent
import io.droidevs.counterapp.ui.vm.states.CounterListUiState
import io.droidevs.counterapp.ui.vm.mappers.toUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountersListViewModel @Inject constructor(
    private val counterUseCases: CounterUseCases
) : ViewModel() {

    private val _event = MutableSharedFlow<CounterListEvent>(extraBufferCapacity = 1)
    val event: SharedFlow<CounterListEvent> = _event.asSharedFlow()

    private val visibleCounters = mutableMapOf<String, Counter>()
    private val pendingReorderCounters = mutableMapOf<String, Counter>()

    val uiState: StateFlow<CounterListUiState> = counterUseCases.getCountersWithCategories()
        .map { counters ->
            counters.map { it.toUiModel() }
        }
        .onStart { emit(emptyList()) } // Emit empty list initially for the mapper
        .map { counters -> counters.toUiState(isLoading = false) }
        .onStart { emit(CounterListUiState(isLoading = true)) } // Initial loading state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CounterListUiState() // Default empty state
        )

    fun onAction(action: CounterListAction) {
        when (action) {
            is CounterListAction.IncrementCounter -> increment(action.counter)
            is CounterListAction.DecrementCounter -> decrement(action.counter)
            is CounterListAction.VisibleItemsChanged -> onVisibleItemsChanged(action.items)
            CounterListAction.AddCounterClicked -> {
                viewModelScope.launch {
                    _event.emit(CounterListEvent.NavigateToCreateCounter)
                }
            }
            is CounterListAction.CounterClicked -> {
                viewModelScope.launch {
                    _event.emit(CounterListEvent.NavigateToCounterView(action.counter.id))
                }
            }
            CounterListAction.FlushAllPendingReorders -> flushAllPendingReorders()
        }
    }

    private fun increment(counterUiModel: CounterUiModel) {
        val c = counterUiModel.toDomain()
        c.increment()
        viewModelScope.launch {
            counterUseCases.updateCounter(UpdateCounterRequest.of(counterId = c.id, newCount = c.currentCount))
        }
        pendingReorderCounters[c.id] = c
        Log.i("CountersListViewModel", "increment: ${pendingReorderCounters.size}")
    }

    private fun decrement(counterUiModel: CounterUiModel) {
        val c = counterUiModel.toDomain()
        c.decrement()
        viewModelScope.launch {
            counterUseCases.updateCounter(UpdateCounterRequest.of(counterId = c.id, newCount = c.currentCount))
        }
        pendingReorderCounters[c.id] = c
    }

    private fun onVisibleItemsChanged(items: Set<CounterUiModel>) {
        val currentlyVisibleKeys = items.map { it.id }.toSet()
        val noLongerVisibleKeys = visibleCounters.keys - currentlyVisibleKeys

        noLongerVisibleKeys.forEach { id ->
            pendingReorderCounters[id]?.let { counter ->
                Log.i("CountersListViewModel", "flushReorder: ${counter.currentCount}")
                flushReorder(counter)
            }
        }

        visibleCounters.clear()
        visibleCounters.putAll(items.associateBy { it.id }.mapValues { it.value.toDomain() })
    }

    private fun flushReorder(counter: Counter) {
        pendingReorderCounters.remove(counter.id)

        viewModelScope.launch {
            counterUseCases.updateCounter(
                UpdateCounterRequest.of(
                    counterId = counter.id,
                    newCount = counter.currentCount
                )
            )
        }
    }

    private fun flushAllPendingReorders() {
        pendingReorderCounters.forEach { _, counter ->
            flushReorder(counter)
        }
        pendingReorderCounters.clear()
    }
}
