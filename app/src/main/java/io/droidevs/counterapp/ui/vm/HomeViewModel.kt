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
import io.droidevs.counterapp.ui.vm.mappers.toHomeUiState
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

    private val _event = MutableSharedFlow<HomeEvent>(extraBufferCapacity = 1)
    val event: SharedFlow<HomeEvent> = _event.asSharedFlow()

    private var activeCounter: Counter? = null
    private var interactionJob: Job? = null

    val uiState: StateFlow<HomeUiState> = combine(
        counterUseCases.getLimitCountersWithCategory(6)
            .map { counters -> counters.map { it.toUiModel() } }
            .onStart { emit(emptyList()) },
        counterUseCases.getTotalNumberOfCounters()
            .onStart { emit(0) },
        categoryUseCases.getTopCategories(3)
            .map { categories -> categories.map { it.toUiModel() } }
            .onStart { emit(emptyList()) },
        categoryUseCases.getTotalCategoriesCount()
            .onStart { emit(0) },
        MutableStateFlow(false).onStart { emit(true) }, // isLoadingCounters
        MutableStateFlow(false).onStart { emit(true) } // isLoadingCategories
    ) { recentCounters, countersCount, categories, categoriesCount, isLoadingCounters, isLoadingCategories ->
        Sixfold(recentCounters, countersCount, categories, categoriesCount, isLoadingCounters, isLoadingCategories).toHomeUiState()
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

data class Sixfold<out A, out B, out C, out D, out E, out F>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E,
    val sixth: F
)
