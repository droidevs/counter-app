package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.usecases.preference.NotificationPreferenceUseCases
import io.droidevs.counterapp.ui.vm.actions.NotificationPreferenceAction
import io.droidevs.counterapp.ui.vm.events.NotificationPreferenceEvent
import io.droidevs.counterapp.ui.vm.states.NotificationPreferenceUiState
import io.droidevs.counterapp.ui.vm.mappers.toNotificationPreferenceUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsPreferencesViewModel @Inject constructor(
    private val useCases: NotificationPreferenceUseCases
) : ViewModel() {

    private val _event = MutableSharedFlow<NotificationPreferenceEvent>(extraBufferCapacity = 1)
    val event: SharedFlow<NotificationPreferenceEvent> = _event.asSharedFlow()

    val uiState: StateFlow<NotificationPreferenceUiState> = combine(
        useCases.getCounterLimitNotification(),
        useCases.getDailySummaryNotification(),
        useCases.getNotificationSound(),
        useCases.getNotificationVibrationPattern()
    ) { limit, daily, sound, vibration ->
        Quadruple(limit, daily, sound, vibration).toNotificationPreferenceUiState()
    }
        .onStart { emit(NotificationPreferenceUiState()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NotificationPreferenceUiState()
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
