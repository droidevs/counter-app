package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.mapResult
import io.droidevs.counterapp.domain.result.onFailure
import io.droidevs.counterapp.domain.result.onSuccessSuspend
import io.droidevs.counterapp.domain.result.recoverWith
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.domain.usecases.category.requests.DeleteCategoryRequest
import io.droidevs.counterapp.domain.usecases.category.requests.GetCategoryWithCountersRequest
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import io.droidevs.counterapp.ui.date.DateFormatter
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.vm.actions.CategoryViewAction
import io.droidevs.counterapp.ui.vm.events.CategoryViewEvent
import io.droidevs.counterapp.ui.vm.mappers.toUiState
import io.droidevs.counterapp.ui.vm.states.CategoryViewUiState
import io.droidevs.counterapp.domain.feedback.CounterFeedbackAction
import io.droidevs.counterapp.domain.feedback.CounterFeedbackManager
import io.droidevs.counterapp.domain.errors.CounterDomainError
import io.droidevs.counterapp.domain.result.flatMapSuspended
import io.droidevs.counterapp.util.TracingHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class CategoryViewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val categoryUseCases: CategoryUseCases,
    private val counterUseCases: CounterUseCases,
    private val dateFormatter: DateFormatter,
    private val uiMessageDispatcher: UiMessageDispatcher,
    private val feedbackManager: CounterFeedbackManager,
    private val tracing: TracingHelper
) : ViewModel() {

    private val categoryId: String = savedStateHandle.get<String>("categoryId")
        ?: throw IllegalArgumentException("CategoryId argument is required")

    private val _event = MutableSharedFlow<CategoryViewEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val event = _event.asSharedFlow()

    private val reorderJobs = mutableMapOf<String, Job>()
    private val pendingReorderCounterIds = mutableSetOf<String>()
    private val reorderIdleMs = 2000L

    val uiState: StateFlow<CategoryViewUiState> = categoryUseCases.getCategoryWithCounters(
        GetCategoryWithCountersRequest(categoryId = categoryId)
    )
        .mapResult { categoryWithCounters ->
            categoryWithCounters
                .toUiModel(dateFormatter)
                .toUiState(isLoading = false, isError = false)
        }
        .onFailure {
            uiMessageDispatcher.dispatch(
                UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_load_category))
            )
        }
        .recoverWith {
            Result.Success(CategoryViewUiState(isLoading = false, isError = true))
        }
        .map { (it as Result.Success).data }
        .onStart { emit(CategoryViewUiState(isLoading = true, isError = false)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CategoryViewUiState(isLoading = true, isError = false)
        )

    fun onAction(action: CategoryViewAction) {
        when (action) {
            is CategoryViewAction.AddCounterClicked -> {
                viewModelScope.launch {
                    _event.emit(CategoryViewEvent.NavigateToCreateCounter(categoryId))
                }
            }
            is CategoryViewAction.DeleteCategoryClicked -> deleteCategory()

            is CategoryViewAction.IncrementCounter -> incrementCounter(action.counter)
            is CategoryViewAction.DecrementCounter -> decrementCounter(action.counter)

            is CategoryViewAction.SetCategoryId -> {
                // Handled by state initialization
            }
        }
    }

    private fun deleteCategory() {
        viewModelScope.launch {
            tracing.tracedSuspend("categoryview_delete_category") {
                categoryUseCases.deleteCategory(
                    DeleteCategoryRequest(categoryId = categoryId)
                )
            }
                .onSuccessSuspend {
                    _event.emit(CategoryViewEvent.NavigateBack)
                }
                .onFailure {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_delete_category))
                    )
                }
        }
    }

    private fun incrementCounter(counter: CounterUiModel) {
        if (!counter.canIncrease) return

        val domain = counter.toDomain()

        viewModelScope.launch {
            tracing.tracedSuspend("categoryview_increment_counter"){
                counterUseCases.incrementCounter(domain)
            }.onSuccessSuspend {
                tracing.tracedSuspend("categoryview_increment_counter_feedback") {
                    feedbackManager.onAction(CounterFeedbackAction.INCREMENT)
                }
            }.onFailure { error ->
                when (error) {
                    CounterDomainError.IncrementBlockedByMaximum -> uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(resId = R.string.counter_maximum_reached))
                    )

                    else -> uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_increment_counter))
                    )
                }
            }
        }

        markPendingReorder(domain)
    }

    private fun decrementCounter(counter: CounterUiModel) {
        if (!counter.canDecrease) return

        val domain = counter.toDomain()

        viewModelScope.launch {
            tracing.tracedSuspend("categoryview_decrement_counter") {
                counterUseCases.decrementCounter(domain)
            }.onSuccessSuspend {
                tracing.tracedSuspend("categoryview_decrement_counter_feedback") {
                    feedbackManager.onAction(CounterFeedbackAction.DECREMENT)
                }
            }
            .onFailure { e ->
                when (e) {
                    CounterDomainError.DecrementBlockedByMinimum -> uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(resId = R.string.counter_minimum_reached))
                    )

                    else -> uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_decrement_counter))
                    )
                }
            }
        }

        markPendingReorder(domain)
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
                // Internal ordering update. Don't toast the user.
                // uiMessageDispatcher.dispatch(UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_update_counter_order)))
            }
        }
    }
}
