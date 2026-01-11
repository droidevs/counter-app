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

    private val initialCategoryId: String? = savedStateHandle.get<String>("categoryId")

    private val _name = MutableStateFlow("")
    private val _canIncrease = MutableStateFlow(true)
    private val _canDecrease = MutableStateFlow(false)
    private val _selectedCategoryId = MutableStateFlow(initialCategoryId)
    private val _isSaving = MutableStateFlow(false)

    private val _event = MutableSharedFlow<CreateCounterEvent>(extraBufferCapacity = 1)
    val event: SharedFlow<CreateCounterEvent> = _event.asSharedFlow()

    private val _categoriesFlow = categoryUseCases.getAllCategories()
        .map { categories -> categories.map { it.toUiModel() } }
        .onStart { emit(emptyList()) } // Emit empty list initially

    val uiState: StateFlow<CreateCounterUiState> = combine(
        _name,
        _canIncrease,
        _canDecrease,
        _selectedCategoryId,
        _categoriesFlow,
        _isSaving
    ) { name, canIncrease, canDecrease, categoryId, categories, isSaving ->
        CreateCounterUiState(
            name = name,
            canIncrease = canIncrease,
            canDecrease = canDecrease,
            categoryId = categoryId,
            categories = categories,
            isSaving = isSaving
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CreateCounterUiState(categoryId = initialCategoryId) // Default state including initialCategoryId
    )

    fun onAction(action: CreateCounterAction) {
        when (action) {
            is CreateCounterAction.NameChanged -> {
                _name.value = action.name
            }
            is CreateCounterAction.CanIncreaseChanged -> {
                _canIncrease.value = action.canIncrease
            }
            is CreateCounterAction.CanDecreaseChanged -> {
                _canDecrease.value = action.canDecrease
            }
            is CreateCounterAction.CategorySelected -> {
                _selectedCategoryId.value = action.categoryId
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
            _isSaving.value = true
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
            _isSaving.value = false
            _event.emit(CreateCounterEvent.CounterCreated(name))
            _event.emit(CreateCounterEvent.NavigateBack)
        }
    }
}
