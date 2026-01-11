package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.usecases.preference.NotificationPreferenceUseCases
import io.droidevs.counterapp.ui.vm.actions.NotificationPreferenceAction
import io.droidevs.counterapp.ui.vm.events.NotificationPreferenceEvent
import io.droidevs.counterapp.ui.vm.states.NotificationPreferenceUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsPreferencesViewModel @Inject constructor(
    private val useCases: NotificationPreferenceUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationPreferenceUiState())
    val uiState: StateFlow<NotificationPreferenceUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<NotificationPreferenceEvent>()
    val event: SharedFlow<NotificationPreferenceEvent> = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            useCases.getCounterLimitNotification().collectLatest { enabled ->
                _uiState.update { it.copy(counterLimitNotification = enabled) }
            }
        }
        viewModelScope.launch {
            useCases.getDailySummaryNotification().collectLatest { enabled ->
                _uiState.update { it.copy(dailySummaryNotification = enabled) }
            }
        }
        viewModelScope.launch {
            useCases.getNotificationSound().collectLatest { sound ->
                _uiState.update { it.copy(notificationSound = sound) }
            }
        }
        viewModelScope.launch {
            useCases.getNotificationVibrationPattern().collectLatest { pattern ->
                _uiState.update { it.copy(notificationVibrationPattern = pattern) }
            }
        }
    }

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
            _event.emit(NotificationPreferenceEvent.ShowMessage("Counter limit notification updated"))
        }
    }

    private fun setDailySummaryNotification(enabled: Boolean) {
        viewModelScope.launch {
            useCases.setDailySummaryNotification(enabled)
            _event.emit(NotificationPreferenceEvent.ShowMessage("Daily summary notification updated"))
        }
    }

    private fun setNotificationSound(sound: String) {
        viewModelScope.launch {
            useCases.setNotificationSound(sound)
            _event.emit(NotificationPreferenceEvent.ShowMessage("Notification sound updated"))
        }
    }

    private fun setNotificationVibrationPattern(pattern: String) {
        viewModelScope.launch {
            useCases.setNotificationVibrationPattern(pattern)
            _event.emit(NotificationPreferenceEvent.ShowMessage("Vibration pattern updated"))
        }
    }
}
