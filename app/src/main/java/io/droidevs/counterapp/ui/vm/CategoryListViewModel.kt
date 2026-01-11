package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.ui.vm.actions.CategoryListAction
import io.droidevs.counterapp.ui.vm.events.CategoryListEvent
import io.droidevs.counterapp.ui.vm.states.CategoryListUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryListUiState())
    val uiState: StateFlow<CategoryListUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CategoryListEvent>()
    val event = _event.asSharedFlow()

    private val isSystem = MutableStateFlow(false)

    val categories: StateFlow<List<io.droidevs.counterapp.ui.models.CategoryUiModel>> = isSystem
        .flatMapLatest { system ->
            if (system) {
                categoryUseCases.getSystemCategories()
            } else {
                categoryUseCases.getAllCategories()
            }
        }
        .map { categories -> categories.map { it.toUiModel() } }
        .onStart { 
            _uiState.update { it.copy(isLoading = true) }
        }
        .onEach { categories ->
            _uiState.update { it.copy(categories = categories, isLoading = false) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
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
                isSystem.value = action.isSystem
                _uiState.update { it.copy(isSystem = action.isSystem) }
            }
        }
    }
}
