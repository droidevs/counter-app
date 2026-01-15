package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.ui.vm.actions.CategoryListAction
import io.droidevs.counterapp.ui.vm.events.CategoryListEvent
import io.droidevs.counterapp.ui.vm.states.CategoryListUiState
import io.droidevs.counterapp.ui.vm.mappers.toUiState
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases,
    private val uiMessageDispatcher: UiMessageDispatcher
) : ViewModel() {

    private val _event = MutableSharedFlow<CategoryListEvent>(extraBufferCapacity = 1)
    val event = _event.asSharedFlow()

    private val _isSystemMode = MutableStateFlow(false)

    val uiState: StateFlow<CategoryListUiState> = _isSystemMode
        .flatMapLatest { isSystem ->
            if (isSystem) {
                categoryUseCases.getSystemCategories()
            } else {
                categoryUseCases.getAllCategories()
            }
                .map { categories -> categories.map { it.toUiModel() } }
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
