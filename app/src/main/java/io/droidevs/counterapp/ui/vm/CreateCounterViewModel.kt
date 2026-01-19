package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.mapResult
import io.droidevs.counterapp.domain.result.onFailure
import io.droidevs.counterapp.domain.result.onSuccess
import io.droidevs.counterapp.domain.result.recoverWith
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.requests.CreateCounterRequest
import io.droidevs.counterapp.ui.date.DateFormatter
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
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
    categoryUseCases: CategoryUseCases,
    private val uiMessageDispatcher: UiMessageDispatcher,
    private val dateFormatter: DateFormatter
) : ViewModel() {

    data class EditModel(
        val name: String = "",
        val canIncrease: Boolean = true,
        val canDecrease: Boolean = false,
        val selectedCategoryId: String? = null,
        val isSaving: Boolean = false,
        val initialValue: Int = 0,

        val useDefaultBehavior: Boolean = true,
        val incrementStepInput: String = "",
        val decrementStepInput: String = "",
        val defaultValueInput: String = "",
        val minValueInput: String = "",
        val maxValueInput: String = "",
    )

    /**
     * Cache for per-counter override text fields.
     * Requirement: when user enables "use default", UI inputs get cleared/hidden but the values are restored when disabled.
     */
    private data class OverridesCache(
        val incrementStepInput: String = "",
        val decrementStepInput: String = "",
        val defaultValueInput: String = "",
        val minValueInput: String = "",
        val maxValueInput: String = "",
    )

    private var overridesCache: OverridesCache = OverridesCache()

    private val initialCategoryId: String? = savedStateHandle.get<String>("categoryId")
    val isCategoryFixed = initialCategoryId != null

    private val _editModel = MutableStateFlow(EditModel(selectedCategoryId = initialCategoryId))

    private val _event = MutableSharedFlow<CreateCounterEvent>(extraBufferCapacity = 1)
    val event: SharedFlow<CreateCounterEvent> = _event.asSharedFlow()

    private data class CategoriesState(
        val categories: List<io.droidevs.counterapp.ui.models.CategoryUiModel> = emptyList(),
        val isError: Boolean = false
    )

    private val _categoriesFlow = categoryUseCases.getAllCategories()
        .mapResult { categories -> categories.map { it.toUiModel(dateFormatter) } }
        .onFailure {
            uiMessageDispatcher.dispatch(
                UiMessage.Toast(message = Message.Resource(R.string.failed_to_load_categories))
            )
        }
        .mapResult { categories ->
            CategoriesState(categories = categories, isError = false)
        }
        .recoverWith {
            Result.Success(CategoriesState(categories = emptyList(), isError = true))
        }
        .map { (it as Result.Success).data }
        .onStart { emit(CategoriesState()) }

    val uiState: StateFlow<CreateCounterUiState> = combine(
        _editModel,
        _categoriesFlow
    ) { editModel: EditModel, categoriesState: CategoriesState ->
        Triple(editModel.name, editModel.canIncrease, editModel.canDecrease).toCreateCounterUiState(
            categoryId = editModel.selectedCategoryId,
            categories = categoriesState.categories,
            isSaving = editModel.isSaving,
            initialValue = editModel.initialValue,
            isInitialValueInputVisible = !editModel.canIncrease && editModel.canDecrease,
            isLoading = false,
            isError = categoriesState.isError,
        ).copy(
            useDefaultBehavior = editModel.useDefaultBehavior,
            incrementStepInput = editModel.incrementStepInput,
            decrementStepInput = editModel.decrementStepInput,
            defaultValueInput = editModel.defaultValueInput,
            minValueInput = editModel.minValueInput,
            maxValueInput = editModel.maxValueInput,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CreateCounterUiState(
            categoryId = initialCategoryId,
            isLoading = true,
            isError = false
        )
    )

    fun onAction(action: CreateCounterAction) {
        when (action) {
            is CreateCounterAction.NameChanged -> _editModel.update { it.copy(name = action.name) }
            is CreateCounterAction.CanIncreaseChanged -> {
                _editModel.update {
                    if (!action.canIncrease) it.copy(canIncrease = false, canDecrease = true)
                    else it.copy(canIncrease = true)
                }
            }
            is CreateCounterAction.CanDecreaseChanged -> {
                _editModel.update {
                    if (!action.canDecrease) it.copy(canDecrease = false, canIncrease = true)
                    else it.copy(canDecrease = true)
                }
            }
            is CreateCounterAction.CategorySelected -> _editModel.update { it.copy(selectedCategoryId = action.categoryId) }
            is CreateCounterAction.InitialValueChanged -> _editModel.update { it.copy(initialValue = action.value) }

            is CreateCounterAction.UseDefaultBehaviorChanged -> {
                val checked = action.value
                _editModel.update { current ->
                    if (checked) {
                        // Save current visible values into cache, then clear exposed fields
                        overridesCache = OverridesCache(
                            incrementStepInput = current.incrementStepInput,
                            decrementStepInput = current.decrementStepInput,
                            defaultValueInput = current.defaultValueInput,
                            minValueInput = current.minValueInput,
                            maxValueInput = current.maxValueInput,
                        )
                        current.copy(
                            useDefaultBehavior = true,
                            incrementStepInput = "",
                            decrementStepInput = "",
                            defaultValueInput = "",
                            minValueInput = "",
                            maxValueInput = "",
                        )
                    } else {
                        // Restore cached values
                        current.copy(
                            useDefaultBehavior = false,
                            incrementStepInput = overridesCache.incrementStepInput,
                            decrementStepInput = overridesCache.decrementStepInput,
                            defaultValueInput = overridesCache.defaultValueInput,
                            minValueInput = overridesCache.minValueInput,
                            maxValueInput = overridesCache.maxValueInput,
                        )
                    }
                }
            }

            is CreateCounterAction.IncrementStepChanged -> _editModel.update { it.copy(incrementStepInput = action.value) }
            is CreateCounterAction.DecrementStepChanged -> _editModel.update { it.copy(decrementStepInput = action.value) }
            is CreateCounterAction.DefaultValueChanged -> _editModel.update { it.copy(defaultValueInput = action.value) }
            is CreateCounterAction.MinValueChanged -> _editModel.update { it.copy(minValueInput = action.value) }
            is CreateCounterAction.MaxValueChanged -> _editModel.update { it.copy(maxValueInput = action.value) }

            CreateCounterAction.SaveClicked -> saveCounter()
        }
    }

    private fun saveCounter() {
        val currentModel = _editModel.value
        val name = currentModel.name.trim()

        if (name.isEmpty()) {
            viewModelScope.launch {
                uiMessageDispatcher.dispatch(
                    UiMessage.Toast(message = Message.Resource(R.string.name_required))
                )
            }
            return
        }

        val initialValue = currentModel.initialValue

        // Parse overrides (ONLY if useDefaultBehavior is false; otherwise these remain null)
        val incrementStep = currentModel.incrementStepInput.trim().toIntOrNull()?.coerceAtLeast(1)
        val decrementStep = currentModel.decrementStepInput.trim().toIntOrNull()?.coerceAtLeast(1)
        val defaultValue = currentModel.defaultValueInput.trim().toIntOrNull()
        val minValue = currentModel.minValueInput.trim().toIntOrNull()
        val maxValue = currentModel.maxValueInput.trim().toIntOrNull()

        val effectiveIncrementStep = if (currentModel.useDefaultBehavior) null else incrementStep
        val effectiveDecrementStep = if (currentModel.useDefaultBehavior) null else decrementStep
        val effectiveDefaultValue = if (currentModel.useDefaultBehavior) null else defaultValue
        val effectiveMinValue = if (currentModel.useDefaultBehavior) null else minValue
        val effectiveMaxValue = if (currentModel.useDefaultBehavior) null else maxValue

        // Validate custom min/max relationship when both set
        if (effectiveMinValue != null && effectiveMaxValue != null && effectiveMinValue > effectiveMaxValue) {
            viewModelScope.launch {
                uiMessageDispatcher.dispatch(
                    UiMessage.Toast(message = Message.Resource(R.string.error_min_gt_max))
                )
            }
            return
        }

        // Validate that current count fits restrictions if restrictions exist.
        if (effectiveMinValue != null && initialValue < effectiveMinValue) {
            viewModelScope.launch {
                uiMessageDispatcher.dispatch(
                    UiMessage.Toast(message = Message.Resource(R.string.error_count_outside_bounds))
                )
            }
            return
        }
        if (effectiveMaxValue != null && initialValue > effectiveMaxValue) {
            viewModelScope.launch {
                uiMessageDispatcher.dispatch(
                    UiMessage.Toast(message = Message.Resource(R.string.error_count_outside_bounds))
                )
            }
            return
        }

        if (!currentModel.canIncrease && currentModel.canDecrease && initialValue <= 0) {
            viewModelScope.launch {
                uiMessageDispatcher.dispatch(
                    UiMessage.Toast(message = Message.Resource(R.string.initial_value_must_be_positive))
                )
                return@launch
            }
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

                incrementStep = effectiveIncrementStep,
                decrementStep = effectiveDecrementStep,
                minValue = effectiveMinValue,
                maxValue = effectiveMaxValue,
                defaultValue = effectiveDefaultValue,
                useDefaultBehavior = currentModel.useDefaultBehavior,

                createdAt = Instant.now(),
                lastUpdatedAt = Instant.now(),
                orderAnchorAt = Instant.now()
            )

            counterUseCases.createCounter(CreateCounterRequest.of(counter))
                .onSuccess { _: Unit ->
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(
                            message = Message.Resource(
                                R.string.counter_created,
                                arrayOf(name)
                            )
                        )
                    )
                    _event.tryEmit(CreateCounterEvent.NavigateBack)
                }
                .onFailure {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(
                            message = Message.Resource(R.string.failed_to_create_counter)
                        )
                    )
                }

            _editModel.update { it.copy(isSaving = false) }
        }
    }
}
