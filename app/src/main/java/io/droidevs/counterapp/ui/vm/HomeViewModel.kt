package io.droidevs.counterapp.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.result.mapResult
import io.droidevs.counterapp.domain.result.onFailure
import io.droidevs.counterapp.domain.result.resultSuspend
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import io.droidevs.counterapp.ui.date.DateFormatter
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.vm.actions.HomeAction
import io.droidevs.counterapp.ui.vm.events.HomeEvent
import io.droidevs.counterapp.ui.vm.mappers.Quadruple
import io.droidevs.counterapp.ui.vm.states.HomeUiState
import io.droidevs.counterapp.ui.vm.mappers.toHomeUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val counterUseCases: CounterUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val dateFormatter : DateFormatter,
    private val uiMessageDispatcher: UiMessageDispatcher
) : ViewModel() {

    private val _event = MutableSharedFlow<HomeEvent>(extraBufferCapacity = 1)
    val event: SharedFlow<HomeEvent> = _event.asSharedFlow()

    private var activeCounter: Counter? = null
    private var interactionJob: Job? = null

    val uiState: StateFlow<HomeUiState> = combine(
        counterUseCases.getLimitCountersWithCategory(6)
            .mapResult { counters -> counters.map { it.toUiModel(dateFormatter) } }
            .onFailure { _ ->
                uiMessageDispatcher.dispatch(
                    UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_load_counter))
                )
            }
            .map { result -> result.getOrNull() ?: emptyList() }
            .onStart { emit(emptyList()) },
        counterUseCases.getTotalNumberOfCounters()
            .map { result -> result.getOrNull() ?: 0 }
            .onStart { emit(0) },
        categoryUseCases.getTopCategories(3)
            .mapResult { categories -> categories.map { it.toUiModel(dateFormatter) } }
            .onFailure { _ ->
                uiMessageDispatcher.dispatch(
                    UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_load_categories))
                )
            }
            .map { result -> result.getOrNull() ?: emptyList() }
            .onStart { emit(emptyList()) },
        categoryUseCases.getTotalCategoriesCount()
            .map { result -> result.getOrNull() ?: 0 }
            .onStart { emit(0) }
    ) { recentCounters, countersCount, categories, categoriesCount ->
        Quadruple(countersCount, categoriesCount, recentCounters.isEmpty(), categories.isEmpty()).toHomeUiState(
            recentCounters = recentCounters,
            categories = categories
        )
    }
        .onStart { emit(HomeUiState(isLoadingCounters = true, isLoadingCategories = true)) } // Initial loading state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState()
        )

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.IncrementCounter -> incrementCounter(action.counter)
            is HomeAction.DecrementCounter -> decrementCounter(action.counter)
            is HomeAction.CounterClicked -> {
                viewModelScope.launch {
                    _event.emit(HomeEvent.NavigateToCounterView(action.counter.id))
                }
            }
            HomeAction.AddCounterClicked -> {
                viewModelScope.launch {
                    _event.emit(HomeEvent.NavigateToCreateCounter)
                }
            }
            is HomeAction.CategoryClicked -> {
                viewModelScope.launch {
                    _event.emit(HomeEvent.NavigateToCategoryView(action.category.id))
                }
            }
            HomeAction.AddCategoryClicked -> {
                viewModelScope.launch {
                    _event.emit(HomeEvent.NavigateToCreateCategory)
                }
            }
            HomeAction.ViewAllCountersClicked -> {
                viewModelScope.launch {
                    _event.emit(HomeEvent.NavigateToCounterList)
                }
            }
            HomeAction.ViewAllCategoriesClicked -> {
                viewModelScope.launch {
                    _event.emit(HomeEvent.NavigateToCategoryList)
                }
            }
        }
    }

    private fun scheduleInteractionEnd(counterId: String) {
        interactionJob?.cancel()

        interactionJob = viewModelScope.launch {
            delay(2000) // user stopped tapping
            finishInteraction(counterId)
        }
    }

    private fun finishInteraction(counterId: String) {
        Log.i("HomeViewModel", "finishInteraction: $counterId")

        viewModelScope.launch {
            counterUseCases.updateCounter(UpdateCounterRequest.of(counterId = counterId, orderAnchorAt = Instant.now()))
                .onFailure { _ ->
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_update_counter))
                    )
                }
        }

        activeCounter = null
    }

    private fun flushInteraction(counterId: String) {
        interactionJob?.cancel()
        viewModelScope.launch {
            counterUseCases.updateCounter(UpdateCounterRequest.of(counterId = counterId, orderAnchorAt = Instant.now()))
                .onFailure { _ ->
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_update_counter))
                    )
                }
        }
    }

    private fun incrementCounter(counterUiModel: CounterUiModel) {
        if (activeCounter != null && activeCounter!!.id != counterUiModel.id) {
            flushInteraction(activeCounter!!.id)
        }

        activeCounter = counterUiModel.toDomain()

        viewModelScope.launch {
            counterUseCases.incrementCounter(counter = activeCounter!!)
                .onFailure { _ ->
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_update_counter))
                    )
                }
        }
        scheduleInteractionEnd(activeCounter!!.id)
    }

    private fun decrementCounter(counterUiModel: CounterUiModel) {
        if (activeCounter != null && activeCounter!!.id != counterUiModel.id) {
            flushInteraction(activeCounter!!.id)
        }
        activeCounter = counterUiModel.toDomain()

        viewModelScope.launch {
            counterUseCases.decrementCounter(counter = activeCounter!!)
                .onFailure { _ ->
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_update_counter))
                    )
                }
        }
        scheduleInteractionEnd(activeCounter!!.id)
    }

    override fun onCleared() {
        activeCounter?.let { flushInteraction(it.id) }
        super.onCleared()
    }
}
