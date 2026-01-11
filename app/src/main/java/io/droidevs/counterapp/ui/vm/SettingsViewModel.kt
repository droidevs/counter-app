package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.ui.vm.actions.SettingsAction
import io.droidevs.counterapp.ui.vm.events.SettingsEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<SettingsEvent>()
    val event: SharedFlow<SettingsEvent> = _event.asSharedFlow()

    fun onAction(action: SettingsAction) {
        viewModelScope.launch {
            when (action) {
                SettingsAction.HardwareClicked -> _event.emit(SettingsEvent.NavigateToHardware)
                SettingsAction.DisplayClicked -> _event.emit(SettingsEvent.NavigateToDisplay)
                SettingsAction.CounterClicked -> _event.emit(SettingsEvent.NavigateToCounter)
                SettingsAction.NotificationClicked -> _event.emit(SettingsEvent.NavigateToNotification)
                SettingsAction.BackupClicked -> _event.emit(SettingsEvent.NavigateToBackup)
                SettingsAction.OtherClicked -> _event.emit(SettingsEvent.NavigateToOther)
            }
        }
    }
}
