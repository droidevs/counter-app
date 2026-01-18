package io.droidevs.counterapp.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.mapResult
import io.droidevs.counterapp.domain.result.onFailure
import io.droidevs.counterapp.domain.result.recoverWith
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import io.droidevs.counterapp.ui.date.DateFormatter
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.vm.actions.CounterListAction
import io.droidevs.counterapp.ui.vm.events.CounterListEvent
import io.droidevs.counterapp.ui.vm.mappers.toUiState
import io.droidevs.counterapp.ui.vm.states.CounterListUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class CountersListViewModel @Inject constructor(
    private val counterUseCases: CounterUseCases,
    private val uiMessageDispatcher: UiMessageDispatcher,
    private val dateFormatter: DateFormatter
) : ViewModel() {

    private val _event = MutableSharedFlow<CounterListEvent>(extraBufferCapacity = 1)
    val event: SharedFlow<CounterListEvent> = _event.asSharedFlow()

    private val visibleCounterIds = mutableSetOf<String>()

    /** IDs pending orderAnchorAt bump after user stops interacting. */
    private val pendingReorderCounterIds = mutableSetOf<String>()

    private val reorderJobs = mutableMapOf<String, Job>()

    private val reorderIdleMs = 2000L

    val uiState: StateFlow<CounterListUiState> = counterUseCases.getCountersWithCategories()
        .mapResult { counters ->
            counters.map { it.toUiModel(dateFormatter) }
        }
        .onFailure {
            uiMessageDispatcher.dispatch(
                UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_load_counter))
            )
        }
        .mapResult { counters -> counters.toUiState(isLoading = false, isError = false) }
        .recoverWith {
            Result.Success(
                emptyList<io.droidevs.counterapp.ui.models.CounterWithCategoryUiModel>()
                    .toUiState(isLoading = false, isError = true)
            )
        }
        .map { (it as Result.Success).data }
        .onStart { emit(CounterListUiState(isLoading = true, isError = false)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CounterListUiState(isLoading = true, isError = false)
        )

    fun onAction(action: CounterListAction) {
        when (action) {
            is CounterListAction.IncrementCounter -> increment(action.counter)
            is CounterListAction.DecrementCounter -> decrement(action.counter)
            is CounterListAction.VisibleItemsChanged -> onVisibleItemsChanged(action.items)
            CounterListAction.AddCounterClicked -> {
                viewModelScope.launch { _event.emit(CounterListEvent.NavigateToCreateCounter) }
            }
            is CounterListAction.CounterClicked -> {
                viewModelScope.launch { _event.emit(CounterListEvent.NavigateToCounterView(action.counter.id)) }
            }
            CounterListAction.FlushAllPendingReorders -> flushAllPendingReorders()
        }
    }

    private fun increment(counterUiModel: CounterUiModel) {
        val c = counterUiModel.toDomain()

        viewModelScope.launch {
            // Step-pref aware + history aware
            counterUseCases.incrementCounter(counter = c)
                .onFailure {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_increment_counter))
                    )
                }
        }

        markPendingReorder(c)
    }

    private fun decrement(counterUiModel: CounterUiModel) {
        val c = counterUiModel.toDomain()

        viewModelScope.launch {
            counterUseCases.decrementCounter(counter = c)
                .onFailure {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_decrement_counter))
                    )
                }
        }

        markPendingReorder(c)
    }

    private fun markPendingReorder(counter: Counter) {
        pendingReorderCounterIds += counter.id
        scheduleReorderFlush(counter.id)
    }

    private fun scheduleReorderFlush(counterId: String) {
        reorderJobs[counterId]?.cancel()
        reorderJobs[counterId] = viewModelScope.launch {
            kotlinx.coroutines.delay(reorderIdleMs)
            if (pendingReorderCounterIds.contains(counterId)) {
                flushReorder(counterId)
            }
        }
    }

    private fun onVisibleItemsChanged(items: Set<CounterUiModel>) {
        val currentlyVisibleKeys = items.map { it.id }.toSet()
        val noLongerVisibleKeys = visibleCounterIds - currentlyVisibleKeys

        noLongerVisibleKeys.forEach { id ->
            if (pendingReorderCounterIds.contains(id)) {
                Log.i("CountersListViewModel", "flushReorder(offscreen): $id")
                flushReorder(id)
            }
        }

        visibleCounterIds.clear()
        visibleCounterIds.addAll(currentlyVisibleKeys)
    }

    private fun flushReorder(counterId: String) {
        reorderJobs[counterId]?.cancel()
        reorderJobs.remove(counterId)
        pendingReorderCounterIds.remove(counterId)

        viewModelScope.launch {
            counterUseCases.updateCounter(
                UpdateCounterRequest.of(
                    counterId = counterId,
                    orderAnchorAt = Instant.now()
                )
            ).onFailure {
                // Internal ordering update. We don't want to toast user for this.
                // (Keep the message key for internal analytics/logging later.)
                // uiMessageDispatcher.dispatch(UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_update_counter_order)))
            }
        }
    }

    private fun flushAllPendingReorders() {
        pendingReorderCounterIds.toList().forEach { flushReorder(it) }
        pendingReorderCounterIds.clear()
        reorderJobs.values.forEach { it.cancel() }
        reorderJobs.clear()
    }
}
