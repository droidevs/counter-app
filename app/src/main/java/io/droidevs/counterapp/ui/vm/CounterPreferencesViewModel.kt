package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.getOrNull
import io.droidevs.counterapp.domain.result.onFailureSuspend
import io.droidevs.counterapp.domain.result.onSuccessSuspend
import io.droidevs.counterapp.domain.usecases.preference.CounterPreferenceUseCases
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.actions.CounterBehaviorPreferenceAction
import io.droidevs.counterapp.ui.vm.states.CounterBehaviorPreferenceUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterPreferencesViewModel @Inject constructor(
    private val useCases: CounterPreferenceUseCases,
    private val uiMessageDispatcher: UiMessageDispatcher
) : ViewModel() {

    val uiState: StateFlow<CounterBehaviorPreferenceUiState> = combine(
        useCases.getCounterIncrementStep(),
        useCases.getCounterDecrementStep(),
        useCases.getDefaultCounterValue(),
        useCases.getMinimumCounterValue(),
        useCases.getMaximumCounterValue()
    ) { incrementStepResult, decrementStepResult, defaultValueResult, minimumValueResult, maximumValueResult ->
        val incrementStep = incrementStepResult.getOrNull() ?: 1
        val decrementStep = decrementStepResult.getOrNull() ?: 1
        val defaultValue = defaultValueResult.getOrNull() ?: 0
        val minimumValue = minimumValueResult.getOrNull()
        val maximumValue = maximumValueResult.getOrNull()

        val hasError = listOf(
            incrementStepResult,
            decrementStepResult,
            defaultValueResult,
            minimumValueResult,
            maximumValueResult
        ).any { it is Result.Failure }

        CounterBehaviorPreferenceUiState(
            counterIncrementStep = incrementStep,
            counterDecrementStep = decrementStep,
            defaultCounterValue = defaultValue,
            minimumCounterValue = minimumValue,
            maximumCounterValue = maximumValue,
            error = hasError,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CounterBehaviorPreferenceUiState(isLoading = true)
    )

    fun onAction(action: CounterBehaviorPreferenceAction) {
        when (action) {
            is CounterBehaviorPreferenceAction.SetCounterIncrementStep -> setIncrementStep(action.step)
            is CounterBehaviorPreferenceAction.SetCounterDecrementStep -> setDecrementStep(action.step)
            is CounterBehaviorPreferenceAction.SetDefaultCounterValue -> setDefaultValue(action.value)
            is CounterBehaviorPreferenceAction.SetMaximumCounterValue -> setMaximumValue(action.value)
            is CounterBehaviorPreferenceAction.SetMinimumCounterValue -> setMinimumValue(action.value)
        }
    }

    private fun setIncrementStep(value: Int) {
        viewModelScope.launch {
            useCases.setCounterIncrementStep(value.coerceAtLeast(1))
                .onSuccessSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(R.string.increment_step_updated))
                    )
                }
                .onFailureSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(R.string.failed_to_update_increment_step))
                    )
                }
        }
    }

    private fun setDecrementStep(value: Int) {
        viewModelScope.launch {
            useCases.setCounterDecrementStep(value.coerceAtLeast(1))
                .onSuccessSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(R.string.decrement_step_updated))
                    )
                }
                .onFailureSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(R.string.failed_to_update_decrement_step))
                    )
                }
        }
    }

    private fun setDefaultValue(value: Int) {
        viewModelScope.launch {
            useCases.setDefaultCounterValue(value)
                .onSuccessSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(R.string.default_value_updated))
                    )
                }
                .onFailureSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(R.string.failed_to_update_default_value))
                    )
                }
        }
    }

    private fun setMinimumValue(value: Int?) {
        viewModelScope.launch {
            useCases.setMinimumCounterValue(value)
                .onSuccessSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(R.string.minimum_value_updated))
                    )
                }
                .onFailureSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(R.string.failed_to_update_minimum_value))
                    )
                }
        }
    }

    private fun setMaximumValue(value: Int?) {
        viewModelScope.launch {
            useCases.setMaximumCounterValue(value)
                .onSuccessSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(R.string.maximum_value_updated))
                    )
                }
                .onFailureSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(R.string.failed_to_update_maximum_value))
                    )
                }
        }
    }
}
