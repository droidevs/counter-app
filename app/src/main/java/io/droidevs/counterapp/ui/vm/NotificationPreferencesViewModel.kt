package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.usecases.preference.NotificationPreferenceUseCases
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.UiMessage.Toast
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.actions.NotificationPreferenceAction
import io.droidevs.counterapp.ui.vm.events.NotificationPreferenceEvent
import io.droidevs.counterapp.ui.vm.mappers.Quadruple
import io.droidevs.counterapp.ui.vm.states.NotificationPreferenceUiState
import io.droidevs.counterapp.ui.vm.mappers.toNotificationPreferenceUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsPreferencesViewModel @Inject constructor(
    private val useCases: NotificationPreferenceUseCases,
    private val uiMessageDispatcher: UiMessageDispatcher
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
            uiMessageDispatcher.dispatch(
                Toast(
                    message = Message.Resource(R.string.counter_limit_notification_updated)
                )
            )
        }
    }

    private fun setDailySummaryNotification(enabled: Boolean) {
        viewModelScope.launch {
            useCases.setDailySummaryNotification(enabled)
            uiMessageDispatcher.dispatch(
                Toast(
                    message = Message.Resource(R.string.daily_summary_notification_updated)
                )
            )
        }
    }

    private fun setNotificationSound(sound: String) {
        viewModelScope.launch {
            useCases.setNotificationSound(sound)
            uiMessageDispatcher.dispatch(
                Toast(
                    message = Message.Resource(R.string.notification_sound_updated)
                )
            )
        }
    }

    private fun setNotificationVibrationPattern(pattern: String) {
        viewModelScope.launch {
            useCases.setNotificationVibrationPattern(pattern)
            uiMessageDispatcher.dispatch(
                Toast(
                    message = Message.Resource(R.string.notification_vibration_pattern_updated)
                )
            )
        }
    }
}
