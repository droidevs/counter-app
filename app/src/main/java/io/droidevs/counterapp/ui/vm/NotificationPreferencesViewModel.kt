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
import io.droidevs.counterapp.domain.usecases.preference.NotificationPreferenceUseCases
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.actions.NotificationPreferenceAction
import io.droidevs.counterapp.ui.vm.events.NotificationPreferenceEvent
import io.droidevs.counterapp.ui.vm.states.NotificationPreferenceUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationPreferencesViewModel @Inject constructor(
    private val useCases: NotificationPreferenceUseCases,
    private val uiMessageDispatcher: UiMessageDispatcher
) : ViewModel() {

    private val _event = MutableSharedFlow<NotificationPreferenceEvent>(extraBufferCapacity = 1)
    val event = _event.asSharedFlow()

    val uiState: StateFlow<NotificationPreferenceUiState> = combine(
        useCases.getCounterLimitNotification(),
        useCases.getDailySummaryNotification(),
        useCases.getNotificationSound(),
        useCases.getNotificationVibrationPattern()
    ) { limitResult, dailyResult, soundResult, vibrationResult ->
        // propagate failures explicitly
        when {
            limitResult is Result.Failure -> Result.Failure(limitResult.error)
            dailyResult is Result.Failure -> Result.Failure(dailyResult.error)
            soundResult is Result.Failure -> Result.Failure(soundResult.error)
            vibrationResult is Result.Failure -> Result.Failure(vibrationResult.error)
            else -> Result.Success(
                NotificationPreferenceUiState(
                    counterLimitNotification = limitResult.dataOr { false },
                    dailySummaryNotification = dailyResult.dataOr { false },
                    notificationSound = soundResult.dataOr { "" },
                    notificationVibrationPattern = vibrationResult.dataOr { "" },
                    error = false,
                    isLoading = false
                )
            )
        }
    }
        .recoverWith {
            Result.Success(
                NotificationPreferenceUiState(
                    counterLimitNotification = false,
                    dailySummaryNotification = false,
                    notificationSound = "",
                    notificationVibrationPattern = "",
                    error = true,
                    isLoading = false
                )
            )
        }
        .map { (it as Result.Success).data }
        .onStart { emit(NotificationPreferenceUiState(isLoading = true)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NotificationPreferenceUiState(isLoading = true)
        )

    fun onAction(action: NotificationPreferenceAction) {
        when (action) {
            is NotificationPreferenceAction.SetCounterLimitNotification -> setCounterLimitNotification(action.enabled)
            is NotificationPreferenceAction.SetDailySummaryNotification -> setDailySummaryNotification(action.enabled)
            is NotificationPreferenceAction.SetNotificationSound -> setNotificationSound(action.soundUri)
            is NotificationPreferenceAction.SetNotificationVibrationPattern -> setNotificationVibrationPattern(action.pattern)
        }
    }

    private fun setCounterLimitNotification(enabled: Boolean) {
        viewModelScope.launch {
            useCases.setCounterLimitNotification(enabled)
                .onSuccessSuspend {
                    uiMessageDispatcher.dispatch(UiMessage.Toast(message = Message.Resource(R.string.counter_limit_notification_updated)))
                }
                .onFailureSuspend {
                    uiMessageDispatcher.dispatch(UiMessage.Toast(message = Message.Resource(R.string.failed_to_update_counter_limit_notification)))
                }
        }
    }

    private fun setDailySummaryNotification(enabled: Boolean) {
        viewModelScope.launch {
            useCases.setDailySummaryNotification(enabled)
                .onSuccessSuspend {
                    uiMessageDispatcher.dispatch(UiMessage.Toast(message = Message.Resource(R.string.daily_summary_notification_updated)))
                }
                .onFailureSuspend {
                    uiMessageDispatcher.dispatch(UiMessage.Toast(message = Message.Resource(R.string.failed_to_update_daily_summary_notification)))
                }
        }
    }

    private fun setNotificationSound(sound: String) {
        viewModelScope.launch {
            useCases.setNotificationSound(sound)
                .onSuccessSuspend {
                    uiMessageDispatcher.dispatch(UiMessage.Toast(message = Message.Resource(R.string.notification_sound_updated)))
                }
                .onFailureSuspend {
                    uiMessageDispatcher.dispatch(UiMessage.Toast(message = Message.Resource(R.string.failed_to_update_notification_sound)))
                }
        }
    }

    private fun setNotificationVibrationPattern(pattern: String) {
        viewModelScope.launch {
            useCases.setNotificationVibrationPattern(pattern)
                .onSuccessSuspend {
                    uiMessageDispatcher.dispatch(UiMessage.Toast(message = Message.Resource(R.string.notification_vibration_pattern_updated)))
                }
                .onFailureSuspend {
                    uiMessageDispatcher.dispatch(UiMessage.Toast(message = Message.Resource(R.string.failed_to_update_notification_vibration_pattern)))
                }
        }
    }
}
