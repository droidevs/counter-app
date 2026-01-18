package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
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
import io.droidevs.counterapp.ui.date.DateFormatter
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.vm.actions.CategoryViewAction
import io.droidevs.counterapp.ui.vm.events.CategoryViewEvent
import io.droidevs.counterapp.ui.vm.mappers.toUiState
import io.droidevs.counterapp.ui.vm.states.CategoryViewUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val categoryUseCases: CategoryUseCases,
    private val counterUseCases: CounterUseCases,
    private val dateFormatter: DateFormatter,
    private val uiMessageDispatcher: UiMessageDispatcher
) : ViewModel() {

    private val categoryId: String = savedStateHandle.get<String>("categoryId")
        ?: throw IllegalArgumentException("CategoryId argument is required")

    private val _event = MutableSharedFlow<CategoryViewEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val event = _event.asSharedFlow()

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
            CategoryViewAction.AddCounterClicked -> {
                viewModelScope.launch {
                    _event.emit(CategoryViewEvent.NavigateToCreateCounter(categoryId))
                }
            }

            CategoryViewAction.DeleteCategoryClicked -> deleteCategory()

            is CategoryViewAction.IncrementCounter -> incrementCounter(action.counter)
            is CategoryViewAction.DecrementCounter -> decrementCounter(action.counter)

            is CategoryViewAction.SetCategoryId -> {
                // Handled by state initialization
            }
        }
    }

    private fun deleteCategory() {
        viewModelScope.launch {
            categoryUseCases.deleteCategory(
                DeleteCategoryRequest(categoryId = categoryId)
            )
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

        viewModelScope.launch {
            counterUseCases.incrementCounter(counter.toDomain())
                .onFailure {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_update_counter))
                    )
                }
        }
    }

    private fun decrementCounter(counter: CounterUiModel) {
        if (!counter.canDecrease) return

        viewModelScope.launch {
            counterUseCases.decrementCounter(counter.toDomain())
                .onFailure {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_update_counter))
                    )
                }
        }
    }
}
