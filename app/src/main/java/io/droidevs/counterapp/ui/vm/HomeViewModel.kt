package io.droidevs.counterapp.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.dataOr
import io.droidevs.counterapp.domain.result.mapResult
import io.droidevs.counterapp.domain.result.onFailure
import io.droidevs.counterapp.domain.result.recoverWith
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import io.droidevs.counterapp.ui.date.DateFormatter
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.vm.actions.HomeAction
import io.droidevs.counterapp.ui.vm.events.HomeEvent
import io.droidevs.counterapp.ui.vm.mappers.Quadruple
import io.droidevs.counterapp.ui.vm.mappers.toHomeUiState
import io.droidevs.counterapp.ui.vm.states.HomeUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val counterUseCases: CounterUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val dateFormatter: DateFormatter,
    private val uiMessageDispatcher: UiMessageDispatcher
) : ViewModel() {

    private val _event = MutableSharedFlow<HomeEvent>(extraBufferCapacity = 1)
    val event = _event.asSharedFlow()

    private var activeCounter: Counter? = null
    private var interactionJob: Job? = null

    private data class HomeCountersState(
        val counters: List<io.droidevs.counterapp.ui.models.CounterWithCategoryUiModel> = emptyList(),
        val count: Int = 0,
        val isLoading: Boolean = false,
        val isError: Boolean = false
    )

    private data class HomeCategoriesState(
        val categories: List<io.droidevs.counterapp.ui.models.CategoryUiModel> = emptyList(),
        val count: Int = 0,
        val isLoading: Boolean = false,
        val isError: Boolean = false
    )

    private val recentCountersState: StateFlow<HomeCountersState> = combine(
        counterUseCases.getLimitCountersWithCategory(6)
            .mapResult { counters -> counters.map { it.toUiModel(dateFormatter) } }
            .onFailure {
                uiMessageDispatcher.dispatch(
                    UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_load_counter))
                )
            },
        counterUseCases.getTotalNumberOfCounters()
    ) { recentResult, countResult ->
        // propagate failures
        when {
            recentResult is Result.Failure -> Result.Failure(recentResult.error)
            countResult is Result.Failure -> Result.Failure(countResult.error)
            else -> Result.Success(
                HomeCountersState(
                    counters = recentResult.dataOr { emptyList() },
                    count = countResult.dataOr { 0 },
                    isLoading = false,
                    isError = false
                )
            )
        }
    }
        .recoverWith {
            Result.Success(HomeCountersState(isLoading = false, isError = true))
        }
        .map { (it as Result.Success).data }
        .onStart { emit(HomeCountersState(isLoading = true)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeCountersState(isLoading = true))

    private val topCategoriesState: StateFlow<HomeCategoriesState> = combine(
        categoryUseCases.getTopCategories(3)
            .mapResult { categories -> categories.map { it.toUiModel(dateFormatter) } }
            .onFailure {
                uiMessageDispatcher.dispatch(
                    UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_load_categories))
                )
            },
        categoryUseCases.getTotalCategoriesCount()
    ) { categoriesResult, countResult ->
        when {
            categoriesResult is Result.Failure -> Result.Failure(categoriesResult.error)
            countResult is Result.Failure -> Result.Failure(countResult.error)
            else -> Result.Success(
                HomeCategoriesState(
                    categories = categoriesResult.dataOr { emptyList() },
                    count = countResult.dataOr { 0 },
                    isLoading = false,
                    isError = false
                )
            )
        }
    }
        .recoverWith {
            Result.Success(HomeCategoriesState(isLoading = false, isError = true))
        }
        .map { (it as Result.Success).data }
        .onStart { emit(HomeCategoriesState(isLoading = true)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeCategoriesState(isLoading = true))

    val uiState: StateFlow<HomeUiState> = combine(
        recentCountersState,
        topCategoriesState
    ) { countersState, categoriesState ->

        Quadruple(
            countersState.count,
            categoriesState.count,
            countersState.isLoading,
            categoriesState.isLoading
        ).toHomeUiState(
            recentCounters = countersState.counters,
            categories = categoriesState.categories,
            isError = countersState.isError || categoriesState.isError
        )
    }
        .onStart { emit(HomeUiState(isLoadingCounters = true, isLoadingCategories = true, isLoading = true, isError = false)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState(isLoadingCounters = true, isLoadingCategories = true, isLoading = true)
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
            delay(2000)
            finishInteraction(counterId)
        }
    }

    private fun finishInteraction(counterId: String) {
        Log.i("HomeViewModel", "finishInteraction: $counterId")

        val counter = activeCounter
        if (counter == null || counter.id != counterId) {
            activeCounter = null
            return
        }

        viewModelScope.launch {
            // IMPORTANT: update orderAnchorAt only after idle delay so list doesn't reorder while user is tapping.
            counterUseCases.updateCounter(
                UpdateCounterRequest.of(
                    counterId = counterId,
                    newCount = counter.currentCount,
                    orderAnchorAt = Instant.now()
                )
            ).onFailure {
                uiMessageDispatcher.dispatch(
                    UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_update_counter))
                )
            }
        }

        activeCounter = null
    }

    private fun flushInteraction(counterId: String) {
        interactionJob?.cancel()

        val counter = activeCounter
        if (counter == null || counter.id != counterId) return

        viewModelScope.launch {
            // Flush both count and orderAnchorAt once.
            counterUseCases.updateCounter(
                UpdateCounterRequest.of(
                    counterId = counterId,
                    newCount = counter.currentCount,
                    orderAnchorAt = Instant.now()
                )
            ).onFailure {
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
                .onFailure {
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
                .onFailure {
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
