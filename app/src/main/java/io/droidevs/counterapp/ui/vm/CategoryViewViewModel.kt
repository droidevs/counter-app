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
import io.droidevs.counterapp.ui.vm.mappers.toUiState
import kotlinx.coroutines.channels.BufferOverflow
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

    private val _event = MutableSharedFlow<CategoryViewEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val event = _event.asSharedFlow()

    val uiState: StateFlow<CategoryViewUiState> = categoryUseCases.getCategoryWithCounters(
        GetCategoryWithCountersRequest(categoryId = categoryId)
    )
        .map { it.toUiModel().toUiState(isLoading = false) }
        .onStart { emit(CategoryViewUiState(isLoading = true)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CategoryViewUiState(isLoading = true)
        )

    fun onAction(action: CategoryViewAction) {
        when (action) {
            CategoryViewAction.AddCounterClicked -> {
                viewModelScope.launch {
                    _event.emit(CategoryViewEvent.NavigateToCreateCounter(categoryId))
                }
            }
            CategoryViewAction.DeleteCategoryClicked -> deleteCategory()
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
            _event.emit(CategoryViewEvent.NavigateBack)
        }
    }
}
