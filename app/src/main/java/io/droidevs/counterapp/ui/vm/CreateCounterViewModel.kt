package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
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

    private val initialCategoryId: String? = savedStateHandle.get<String>("categoryId")

    private val _uiState = MutableStateFlow(CreateCounterUiState(categoryId = initialCategoryId))
    val uiState: StateFlow<CreateCounterUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CreateCounterEvent>()
    val event: SharedFlow<CreateCounterEvent> = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            categoryUseCases.getAllCategories()
                .map { categories -> categories.map { it.toUiModel() } }
                .collect { categories ->
                    _uiState.update { it.copy(categories = categories) }
                }
        }
    }

    fun onAction(action: CreateCounterAction) {
        when (action) {
            is CreateCounterAction.NameChanged -> {
                _uiState.update { it.copy(name = action.name) }
            }
            is CreateCounterAction.CanIncreaseChanged -> {
                _uiState.update { it.copy(canIncrease = action.canIncrease) }
            }
            is CreateCounterAction.CanDecreaseChanged -> {
                _uiState.update { it.copy(canDecrease = action.canDecrease) }
            }
            is CreateCounterAction.CategorySelected -> {
                _uiState.update { it.copy(categoryId = action.categoryId) }
            }
            CreateCounterAction.SaveClicked -> saveCounter()
        }
    }

    private fun saveCounter() {
        val currentUiState = _uiState.value
        val name = currentUiState.name.trim()

        if (name.isEmpty()) {
            viewModelScope.launch {
                _event.emit(CreateCounterEvent.ShowMessage("Name is required"))
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
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
            _uiState.update { it.copy(isSaving = false) }
            _event.emit(CreateCounterEvent.CounterCreated(name))
            _event.emit(CreateCounterEvent.NavigateBack)
        }
    }
}
