package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.domain.usecases.category.requests.CreateCategoryRequest
import io.droidevs.counterapp.domain.model.CategoryColor
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.actions.CreateCategoryAction
import io.droidevs.counterapp.ui.vm.events.CreateCategoryEvent
import io.droidevs.counterapp.ui.vm.states.CreateCategoryUiState
import io.droidevs.counterapp.domain.result.onFailure
import io.droidevs.counterapp.domain.result.onSuccess
import io.droidevs.counterapp.util.TracingHelper
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCategoryViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases,
    private val uiMessageDispatcher: UiMessageDispatcher,
    private val tracing : TracingHelper
) : ViewModel() {

    private val _name = MutableStateFlow("")
    private val _selectedColor = MutableStateFlow(0)
    private val _colors = MutableStateFlow<List<CategoryColor>>(emptyList())
    private val _isSaving = MutableStateFlow(false)

    private val _event = MutableSharedFlow<CreateCategoryEvent>(extraBufferCapacity = 1)
    val event = _event.asSharedFlow()

    val uiState: StateFlow<CreateCategoryUiState> = combine(
        _name,
        _selectedColor,
        _colors,
        _isSaving
    ) { name, selectedColor, colors, isSaving ->
        CreateCategoryUiState(
            name = name,
            selectedColor = selectedColor,
            colors = colors,
            isSaving = isSaving
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CreateCategoryUiState()
    )

    fun onAction(action: CreateCategoryAction) {
        when (action) {
            is CreateCategoryAction.NameChanged -> {
                _name.value = action.name
            }
            is CreateCategoryAction.ColorSelected -> {
                _selectedColor.value = action.colorInt
            }
            CreateCategoryAction.CreateClicked -> saveCategory()
            is CreateCategoryAction.LoadPalette -> {
                _colors.value = action.colors
            }
        }
    }

    private fun saveCategory() {
        val name = _name.value.trim()
        if (name.isEmpty()) {
            uiMessageDispatcher.dispatch(
                UiMessage.Toast(
                    message = Message.Resource(R.string.category_name_required)
                )
            )
            return
        }

        viewModelScope.launch {
            _isSaving.value = true

            tracing.tracedSuspend("create_category_save") {
                categoryUseCases.createCategory(
                    CreateCategoryRequest.of(
                        name = name,
                        color = _selectedColor.value
                    )
                )
                    .onSuccess {
                        uiMessageDispatcher.dispatch(
                            UiMessage.Toast(
                                message = Message.Resource(
                                    resId = R.string.category_created_message,
                                    args = arrayOf(name)
                                )
                            )
                        )
                        _event.tryEmit(CreateCategoryEvent.NavigateBack)
                    }
                    .onFailure { _ ->
                        uiMessageDispatcher.dispatch(
                            UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_create_category))
                        )
                    }
            }

            _isSaving.value = false
        }
    }
}
