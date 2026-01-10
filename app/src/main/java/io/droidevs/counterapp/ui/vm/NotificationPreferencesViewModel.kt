package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.usecases.preference.NotificationPreferenceUseCases
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsPreferencesViewModel @Inject constructor(
    private val useCases: NotificationPreferenceUseCases
) : ViewModel() {

    val counterLimitNotification: StateFlow<Boolean> = useCases.getCounterLimitNotification()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val dailySummaryNotification: StateFlow<Boolean> = useCases.getDailySummaryNotification()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val notificationSound: StateFlow<String> = useCases.getNotificationSound()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "default"
        )

    val vibrationPattern: StateFlow<String> = useCases.getNotificationVibrationPattern()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "default"
        )

    // Update methods
    fun setCounterLimitNotification(enabled: Boolean) {
        viewModelScope.launch {
            useCases.setCounterLimitNotification(enabled)
        }
    }

    fun setDailySummaryNotification(enabled: Boolean) {
        viewModelScope.launch {
            useCases.setDailySummaryNotification(enabled)
        }
    }

    fun setNotificationSound(sound: String) {
        viewModelScope.launch {
            useCases.setNotificationSound(sound)
        }
    }

    fun setNotificationVibrationPattern(pattern: String) {
        viewModelScope.launch {
            useCases.setNotificationVibrationPattern(pattern)
        }
    }
}