package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.mapResult
import io.droidevs.counterapp.domain.result.onFailure
import io.droidevs.counterapp.domain.result.recoverWith
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.ui.date.DateFormatter
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.actions.CategoryListAction
import io.droidevs.counterapp.ui.vm.events.CategoryListEvent
import io.droidevs.counterapp.ui.vm.mappers.toUiState
import io.droidevs.counterapp.ui.vm.states.CategoryListUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases,
    private val uiMessageDispatcher: UiMessageDispatcher,
    private val dateFormatter: DateFormatter
) : ViewModel() {

    private val _event = MutableSharedFlow<CategoryListEvent>(extraBufferCapacity = 1)
    val event = _event.asSharedFlow()

    private val _isSystemMode = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<CategoryListUiState> = _isSystemMode
        .flatMapLatest { isSystem ->
            val flow = if (isSystem) {
                categoryUseCases.getSystemCategories()
            } else {
                categoryUseCases.getAllCategories()
            }

            flow
                .mapResult { categories ->
                    categories.map {
                        it.toUiModel(dateFormatter).copy(
                            createdTime = it.createdAt?.let { instant -> dateFormatter.format(instant) },
                            editedTime = it.updatedAt?.let { instant -> dateFormatter.format(instant) }
                        )
                    }
                }
                .onFailure {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_load_categories))
                    )
                }
                .mapResult { categories ->
                    categories.toUiState(isLoading = false, isError = false, isSystem = isSystem)
                }
                .recoverWith {
                    Result.Success(
                        emptyList<io.droidevs.counterapp.ui.models.CategoryUiModel>()
                            .toUiState(isLoading = false, isError = true, isSystem = isSystem)
                    )
                }
                .map { (it as Result.Success).data }
                .onStart { emit(CategoryListUiState(isLoading = true, isError = false, isSystem = isSystem)) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CategoryListUiState(isLoading = true)
        )

    fun onAction(action: CategoryListAction) {
        when (action) {
            is CategoryListAction.CategoryClicked -> {
                viewModelScope.launch {
                    _event.emit(CategoryListEvent.NavigateToCategoryView(action.category.id))
                }
            }

            CategoryListAction.CreateCategoryClicked -> {
                viewModelScope.launch {
                    _event.emit(CategoryListEvent.NavigateToCreateCategory)
                }
            }

            is CategoryListAction.SetSystemMode -> {
                _isSystemMode.value = action.isSystem
            }
        }
    }
}
