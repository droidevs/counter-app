package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.domain.usecases.requests.CreateCounterRequest
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.ui.vm.actions.CreateCounterAction
import io.droidevs.counterapp.ui.vm.events.CreateCounterEvent
import io.droidevs.counterapp.ui.vm.states.CreateCounterUiState
import io.droidevs.counterapp.ui.vm.mappers.toCreateCounterUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class CreateCounterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val counterUseCases: CounterUseCases,
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    // Single model data class
    data class EditModel(
        val name: String = "",
        val canIncrease: Boolean = true,
        val canDecrease: Boolean = false,
        val selectedCategoryId: String? = null,
        val isSaving: Boolean = false
    )

    private val initialCategoryId: String? = savedStateHandle.get<String>("categoryId")

    // Single MutableStateFlow for the entire model
    private val _editModel = MutableStateFlow(EditModel(selectedCategoryId = initialCategoryId))

    private val _event = MutableSharedFlow<CreateCounterEvent>(extraBufferCapacity = 1)
    val event: SharedFlow<CreateCounterEvent> = _event.asSharedFlow()

    private val _categoriesFlow = categoryUseCases.getAllCategories()
        .map { categories -> categories.map { it.toUiModel() } }
        .onStart { emit(emptyList()) } // Emit empty list initially

    // Combine the single model state flow with categories
    val uiState: StateFlow<CreateCounterUiState> = combine(
        _editModel,
        _categoriesFlow
    ) { editModel, categories ->
        Triple(editModel.name, editModel.canIncrease, editModel.canDecrease).toCreateCounterUiState(
            categoryId = editModel.selectedCategoryId,
            categories = categories,
            isSaving = editModel.isSaving
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CreateCounterUiState(categoryId = initialCategoryId) // Default state including initialCategoryId
    )

    fun onAction(action: CreateCounterAction) {
        when (action) {
            is CreateCounterAction.NameChanged -> {
                _editModel.update { it.copy(name = action.name) }
            }
            is CreateCounterAction.CanIncreaseChanged -> {
                _editModel.update { it.copy(canIncrease = action.canIncrease) }
            }
            is CreateCounterAction.CanDecreaseChanged -> {
                _editModel.update { it.copy(canDecrease = action.canDecrease) }
            }
            is CreateCounterAction.CategorySelected -> {
                _editModel.update { it.copy(selectedCategoryId = action.categoryId) }
            }
            CreateCounterAction.SaveClicked -> saveCounter()
        }
    }

    private fun saveCounter() {
        val currentUiState = uiState.value
        val name = currentUiState.name.trim()

        if (name.isEmpty()) {
            viewModelScope.launch {
                _event.emit(CreateCounterEvent.ShowMessage("Name is required"))
            }
            return
        }

        viewModelScope.launch {
            // Update isSaving in the model
            _editModel.update { it.copy(isSaving = true) }

            val counter = Counter(
                name = name,
                currentCount = 0,
                categoryId = currentUiState.categoryId,
                canIncrease = currentUiState.canIncrease,
                canDecrease = currentUiState.canDecrease,
                createdAt = Instant.now(),
                lastUpdatedAt = Instant.now(),
                orderAnchorAt = Instant.now()
            )

            counterUseCases.createCounter(CreateCounterRequest.of(counter))

            // Reset isSaving
            _editModel.update { it.copy(isSaving = false) }

            _event.emit(CreateCounterEvent.CounterCreated(name))
            _event.emit(CreateCounterEvent.NavigateBack)
        }
    }
}