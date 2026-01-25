package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.dataOr
import io.droidevs.counterapp.domain.result.onFailureSuspend
import io.droidevs.counterapp.domain.result.onSuccessSuspend
import io.droidevs.counterapp.domain.result.recoverWith
import io.droidevs.counterapp.domain.usecases.preference.CounterPreferenceUseCases
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.actions.CounterBehaviorPreferenceAction
import io.droidevs.counterapp.ui.vm.states.CounterBehaviorPreferenceUiState
import io.droidevs.counterapp.util.TracingHelper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterPreferencesViewModel @Inject constructor(
    private val useCases: CounterPreferenceUseCases,
    private val uiMessageDispatcher: UiMessageDispatcher,
    private val tracing: TracingHelper
) : ViewModel() {

    val uiState: StateFlow<CounterBehaviorPreferenceUiState> = combine(
        useCases.getCounterIncrementStep(),
        useCases.getCounterDecrementStep(),
        useCases.getDefaultCounterValue(),
        useCases.getMinimumCounterValue(),
        useCases.getMaximumCounterValue()
    ) { incrementStepResult, decrementStepResult, defaultValueResult, minimumValueResult, maximumValueResult ->

        when {
            incrementStepResult is Result.Failure -> Result.Failure(incrementStepResult.error)
            decrementStepResult is Result.Failure -> Result.Failure(decrementStepResult.error)
            defaultValueResult is Result.Failure -> Result.Failure(defaultValueResult.error)
            minimumValueResult is Result.Failure -> Result.Failure(minimumValueResult.error)
            maximumValueResult is Result.Failure -> Result.Failure(maximumValueResult.error)
            else -> Result.Success(
                CounterBehaviorPreferenceUiState(
                    counterIncrementStep = incrementStepResult.dataOr { 1 },
                    counterDecrementStep = decrementStepResult.dataOr { 1 },
                    defaultCounterValue = defaultValueResult.dataOr { 0 },
                    minimumCounterValue = minimumValueResult.dataOr { null },
                    maximumCounterValue = maximumValueResult.dataOr { null },
                    error = false,
                    isLoading = false
                )
            )
        }
    }
        .recoverWith {
            Result.Success(
                CounterBehaviorPreferenceUiState(
                    counterIncrementStep = 1,
                    counterDecrementStep = 1,
                    defaultCounterValue = 0,
                    minimumCounterValue = null,
                    maximumCounterValue = null,
                    error = true,
                    isLoading = false
                )
            )
        }
        .map { (it as Result.Success).data }
        .onStart { emit(CounterBehaviorPreferenceUiState(isLoading = true)) }
        .stateIn(
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
            tracing.tracedSuspend("prefs_set_counter_increment_step") {
                useCases.setCounterIncrementStep(value.coerceAtLeast(1))
            }
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
            tracing.tracedSuspend("prefs_set_counter_decrement_step"){
                useCases.setCounterDecrementStep(value.coerceAtLeast(1))
            }
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
            tracing.tracedSuspend("prefs_set_default_counter_value") {
                useCases.setDefaultCounterValue(value)
            }
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
            tracing.tracedSuspend("prefs_set_minimum_counter_value") {
                useCases.setMinimumCounterValue(value)
            }
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
            tracing.tracedSuspend("prefs_set_maximum_counter_value") {
                useCases.setMaximumCounterValue(value)
            }
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
