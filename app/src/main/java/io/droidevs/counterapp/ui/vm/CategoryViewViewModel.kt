package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.domain.usecases.category.requests.DeleteCategoryRequest
import io.droidevs.counterapp.domain.usecases.category.requests.GetCategoryWithCountersRequest
import io.droidevs.counterapp.ui.vm.actions.CategoryViewAction
import io.droidevs.counterapp.ui.vm.events.CategoryViewEvent
import io.droidevs.counterapp.ui.vm.states.CategoryViewUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    private val categoryId: String = savedStateHandle.get<String>("categoryId")
        ?: throw IllegalArgumentException("CategoryId argument is required")

    private val _uiState = MutableStateFlow(CategoryViewUiState())
    val uiState: StateFlow<CategoryViewUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CategoryViewEvent>()
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            categoryUseCases.getCategoryWithCounters(
                GetCategoryWithCountersRequest(categoryId = categoryId)
            )
            .onStart { _uiState.update { it.copy(isLoading = true) } }
            .map { it.toUiModel() }
            .collect { data ->
                _uiState.update { 
                    it.copy(
                        category = data.category,
                        counters = data.counters,
                        isLoading = false,
                        showEmptyState = data.counters.isEmpty()
                    ) 
                }
            }
        }
    }

    fun onAction(action: CategoryViewAction) {
        when (action) {
            CategoryViewAction.AddCounterClicked -> {
                viewModelScope.launch {
                    _event.emit(CategoryViewEvent.NavigateToCreateCounter(categoryId))
                }
            }
            CategoryViewAction.DeleteCategoryClicked -> deleteCategory()
            is CategoryViewAction.SetCategoryId -> {
                // Already handled in init via SavedStateHandle, but could be used for re-loading
            }
        }
    }

    private fun deleteCategory() {
        viewModelScope.launch {
            categoryUseCases.deleteCategory(
                DeleteCategoryRequest(categoryId = categoryId)
            )
            _event.emit(CategoryViewEvent.NavigateBack)
        }
    }
}
