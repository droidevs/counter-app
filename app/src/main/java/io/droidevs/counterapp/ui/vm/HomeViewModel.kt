package io.droidevs.counterapp.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toParcelable
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.vm.actions.HomeAction
import io.droidevs.counterapp.ui.vm.events.HomeEvent
import io.droidevs.counterapp.ui.vm.states.HomeUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val counterUseCases: CounterUseCases,
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<HomeEvent>()
    val event: SharedFlow<HomeEvent> = _event.asSharedFlow()

    private var activeCounter: Counter? = null
    private var interactionJob: Job? = null

    init {
        viewModelScope.launch {
            counterUseCases.getLimitCountersWithCategory(6)
                .onStart { _uiState.update { it.copy(isLoadingCounters = true) } }
                .map { counters -> counters.map { it.toUiModel() } }
                .collect { counters ->
                    _uiState.update { it.copy(recentCounters = counters, isLoadingCounters = false) }
                }
        }

        viewModelScope.launch {
            counterUseCases.getTotalNumberOfCounters()
                .collect { count ->
                    _uiState.update { it.copy(countersCount = count) }
                }
        }

        viewModelScope.launch {
            categoryUseCases.getTopCategories(3)
                .onStart { _uiState.update { it.copy(isLoadingCategories = true) } }
                .map { categories -> categories.map { it.toUiModel() } }
                .collect { categories ->
                    _uiState.update { it.copy(categories = categories, isLoadingCategories = false) }
                }
        }

        viewModelScope.launch {
            categoryUseCases.getTotalCategoriesCount()
                .collect { count ->
                    _uiState.update { it.copy(categoriesCount = count) }
                }
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.IncrementCounter -> incrementCounter(action.counter)
            is HomeAction.DecrementCounter -> decrementCounter(action.counter)
            is HomeAction.CounterClicked -> {
                viewModelScope.launch {
                    _event.emit(HomeEvent.NavigateToCounterView(action.counter.toParcelable()))
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

    private fun scheduleInteractionEnd(counter: Counter) {
        interactionJob?.cancel()

        interactionJob = viewModelScope.launch {
            delay(2000) // user stopped tapping
            finishInteraction(counter)
        }
    }

    private fun finishInteraction(counter: Counter) {
        Log.i("HomeViewModel", "finishInteraction: ${counter.currentCount}")
        val updatedCounter = counter.copy(
            orderAnchorAt = Instant.now()
        )
        viewModelScope.launch {
            counterUseCases.updateCounter(UpdateCounterRequest.of(counterId = updatedCounter.id, newCount = updatedCounter.currentCount, orderAnchorAt = updatedCounter.orderAnchorAt))
        }
        activeCounter = null
    }

    private fun flushInteraction(counter: Counter) {
        interactionJob?.cancel()
        val updatedCounter = counter.copy(orderAnchorAt = Instant.now())
        viewModelScope.launch {
            counterUseCases.updateCounter(UpdateCounterRequest.of(counterId = updatedCounter.id, newCount = updatedCounter.currentCount, orderAnchorAt = updatedCounter.orderAnchorAt))
        }
    }

    private fun incrementCounter(counterUiModel: CounterUiModel) {
        if (activeCounter != null && activeCounter!!.id != counterUiModel.id) {
            flushInteraction(activeCounter!!)
        }

        activeCounter = counterUiModel.toDomain()
        activeCounter?.increment()
        val updatedCounter = activeCounter!!

        // Optimistically update the UI state
        _uiState.update { currentState ->
            val updatedList = currentState.recentCounters.map { 
                if (it.counter.id == updatedCounter.id) it.copy(counter = it.counter.copy(currentCount = updatedCounter.currentCount, lastUpdatedAt = updatedCounter.lastUpdatedAt)) else it
            }
            currentState.copy(recentCounters = updatedList)
        }

        viewModelScope.launch {
            counterUseCases.updateCounter(UpdateCounterRequest.of(counterId = updatedCounter.id, newCount = updatedCounter.currentCount))
        }
        scheduleInteractionEnd(updatedCounter)
    }

    private fun decrementCounter(counterUiModel: CounterUiModel) {
        if (activeCounter != null && activeCounter!!.id != counterUiModel.id) {
            flushInteraction(activeCounter!!)
        }
        activeCounter = counterUiModel.toDomain()
        activeCounter?.decrement()
        val updatedCounter = activeCounter!!

        // Optimistically update the UI state
        _uiState.update { currentState ->
            val updatedList = currentState.recentCounters.map { 
                if (it.counter.id == updatedCounter.id) it.copy(counter = it.counter.copy(currentCount = updatedCounter.currentCount, lastUpdatedAt = updatedCounter.lastUpdatedAt)) else it
            }
            currentState.copy(recentCounters = updatedList)
        }

        viewModelScope.launch {
            counterUseCases.updateCounter(UpdateCounterRequest.of(counterId = updatedCounter.id, newCount = updatedCounter.currentCount))
        }
        scheduleInteractionEnd(updatedCounter)
    }

    override fun onCleared() {
        activeCounter?.let { flushInteraction(it) }
        super.onCleared()
    }
}
