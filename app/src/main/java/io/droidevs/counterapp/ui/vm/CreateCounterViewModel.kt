package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.requests.CreateCounterRequest
import io.droidevs.counterapp.ui.vm.actions.CreateCounterAction
import io.droidevs.counterapp.ui.vm.events.CreateCounterEvent
import io.droidevs.counterapp.ui.vm.mappers.toCreateCounterUiState
import io.droidevs.counterapp.ui.vm.states.CreateCounterUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class CreateCounterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val counterUseCases: CounterUseCases,
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    data class EditModel(
        val name: String = "",
        val canIncrease: Boolean = true,
        val canDecrease: Boolean = false,
        val selectedCategoryId: String? = null,
        val isSaving: Boolean = false,
        val initialValue: Int = 0
    )

    private val initialCategoryId: String? = savedStateHandle.get<String>("categoryId")
    val isCategoryFixed = initialCategoryId != null

    private val _editModel = MutableStateFlow(EditModel(selectedCategoryId = initialCategoryId))

    private val _event = MutableSharedFlow<CreateCounterEvent>(extraBufferCapacity = 1)
    val event: SharedFlow<CreateCounterEvent> = _event.asSharedFlow()

    private val _categoriesFlow = categoryUseCases.getAllCategories()
        .map { categories -> categories.map { it.toUiModel() } }
        .onStart { emit(emptyList()) }

    val uiState: StateFlow<CreateCounterUiState> = combine(
        _editModel,
        _categoriesFlow
    ) { editModel, categories ->
        Triple(editModel.name, editModel.canIncrease, editModel.canDecrease).toCreateCounterUiState(
            categoryId = editModel.selectedCategoryId,
            categories = categories,
            isSaving = editModel.isSaving,
            initialValue = editModel.initialValue,
            isInitialValueInputVisible = !editModel.canIncrease && editModel.canDecrease
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CreateCounterUiState(
            categoryId = initialCategoryId
        )
    )

    fun onAction(action: CreateCounterAction) {
        when (action) {
            is CreateCounterAction.NameChanged -> _editModel.update { it.copy(name = action.name) }
            is CreateCounterAction.CanIncreaseChanged -> {
                val newState = _editModel.value.copy(canIncrease = action.canIncrease)
                if (!newState.canIncrease) newState.copy(canDecrease = true) else newState
                _editModel.update { if (!action.canIncrease) it.copy(canIncrease = false, canDecrease = true) else it.copy(canIncrease = true) }
            }
            is CreateCounterAction.CanDecreaseChanged -> {
                _editModel.update { if (!action.canDecrease) it.copy(canDecrease = false, canIncrease = true) else it.copy(canDecrease = true) }
            }
            is CreateCounterAction.CategorySelected -> _editModel.update { it.copy(selectedCategoryId = action.categoryId) }
            is CreateCounterAction.InitialValueChanged -> _editModel.update { it.copy(initialValue = action.value) }
            CreateCounterAction.SaveClicked -> saveCounter()
        }
    }

    private fun saveCounter() {
        val currentModel = _editModel.value
        val name = currentModel.name.trim()

        if (name.isEmpty()) {
            viewModelScope.launch { _event.tryEmit(CreateCounterEvent.ShowMessage("Name is required")) }
            return
        }

        val initialValue = currentModel.initialValue ?: 0

        if (!currentModel.canIncrease && currentModel.canDecrease && initialValue <= 0) {
            viewModelScope.launch { _event.tryEmit(CreateCounterEvent.ShowMessage("Initial value must be greater than 0")) }
            return
        }

        viewModelScope.launch {
            _editModel.update { it.copy(isSaving = true) }

            val counter = Counter(
                name = name,
                currentCount = initialValue,
                categoryId = currentModel.selectedCategoryId,
                canIncrease = currentModel.canIncrease,
                canDecrease = currentModel.canDecrease,
                createdAt = Instant.now(),
                lastUpdatedAt = Instant.now(),
                orderAnchorAt = Instant.now()
            )

            counterUseCases.createCounter(CreateCounterRequest.of(counter))
            _editModel.update { it.copy(isSaving = false) }

            _event.tryEmit(CreateCounterEvent.CounterCreated(name))
            _event.tryEmit(CreateCounterEvent.NavigateBack)
        }
    }
}
