package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.domain.usecases.category.requests.CreateCategoryRequest
import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.model.CategoryColor
import io.droidevs.counterapp.ui.vm.actions.CreateCategoryAction
import io.droidevs.counterapp.ui.vm.events.CreateCategoryEvent
import io.droidevs.counterapp.ui.vm.states.CreateCategoryUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCategoryViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateCategoryUiState())
    val uiState: StateFlow<CreateCategoryUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CreateCategoryEvent>()
    val event = _event.asSharedFlow()

    fun onAction(action: CreateCategoryAction) {
        when (action) {
            is CreateCategoryAction.NameChanged -> {
                _uiState.update { it.copy(name = action.name) }
            }
            is CreateCategoryAction.ColorSelected -> {
                _uiState.update { it.copy(selectedColor = action.colorInt) }
            }
            CreateCategoryAction.CreateClicked -> saveCategory()
            is CreateCategoryAction.LoadPalette -> {
                _uiState.update { it.copy(colors = action.colors) }
            }
        }
    }

    private fun saveCategory() {
        val name = _uiState.value.name.trim()
        if (name.isEmpty()) {
            viewModelScope.launch {
                _event.emit(CreateCategoryEvent.ShowMessage("Category name is required"))
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val category = Category(
                name = name,
                color = CategoryColor(colorInt = _uiState.value.selectedColor)
            )
            categoryUseCases.createCategory(CreateCategoryRequest.of(category))
            _uiState.update { it.copy(isSaving = false) }
            _event.emit(CreateCategoryEvent.CategoryCreated(name))
            _event.emit(CreateCategoryEvent.NavigateBack)
        }
    }
}
