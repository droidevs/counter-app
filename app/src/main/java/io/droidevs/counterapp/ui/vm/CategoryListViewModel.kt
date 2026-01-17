package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.domain.result.mapResult
import io.droidevs.counterapp.domain.result.onFailure
import io.droidevs.counterapp.ui.date.DateFormatter
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.actions.CategoryListAction
import io.droidevs.counterapp.ui.vm.events.CategoryListEvent
import io.droidevs.counterapp.ui.vm.states.CategoryListUiState
import io.droidevs.counterapp.ui.vm.mappers.toUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases,
    private val uiMessageDispatcher: UiMessageDispatcher,
    private val dateFormatter : DateFormatter
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
                            createdTime = it.createdAt?.let { dateFormatter.format(it) },
                            editedTime = it.updatedAt?.let { dateFormatter.format(it) }
                        )
                    }
                }
                .onFailure { _ ->
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_load_categories))
                    )
                }
                .map { result -> result.getOrNull() ?: emptyList() }
                .onStart { emit(emptyList()) } // Emit empty list initially for the mapper
                .map { categories -> categories.toUiState(isLoading = false, isSystem = isSystem) }
                .onStart { emit(CategoryListUiState(isLoading = true, isSystem = isSystem)) } // Initial loading state
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CategoryListUiState() // Default empty state
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
