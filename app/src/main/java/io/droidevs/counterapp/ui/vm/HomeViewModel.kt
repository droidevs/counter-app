package io.droidevs.counterapp.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.vm.actions.HomeAction
import io.droidevs.counterapp.ui.vm.events.HomeEvent
import io.droidevs.counterapp.ui.vm.mappers.Quadruple
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
            .onStart { emit(0) }
    ) { recentCounters, countersCount, categories, categoriesCount ->
        // The isLoading states are now derived from the presence of data, or can be managed within the mapper
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
        }

        activeCounter = null
    }

    private fun flushInteraction(counterId: String) {
        interactionJob?.cancel()
        viewModelScope.launch {
            counterUseCases.updateCounter(UpdateCounterRequest.of(counterId = counterId, orderAnchorAt = Instant.now()))
        }
    }

    private fun incrementCounter(counterUiModel: CounterUiModel) {
        if (activeCounter != null && activeCounter!!.id != counterUiModel.id) {
            flushInteraction(activeCounter!!.id)
        }

        activeCounter = counterUiModel.toDomain()

        viewModelScope.launch {
            counterUseCases.incrementCounter(counter = activeCounter!!)
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
        }
        scheduleInteractionEnd(activeCounter!!.id)
    }

    override fun onCleared() {
        activeCounter?.let { flushInteraction(it.id) }
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
