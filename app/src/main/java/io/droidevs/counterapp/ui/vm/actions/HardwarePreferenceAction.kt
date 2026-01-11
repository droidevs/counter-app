package io.droidevs.counterapp.ui.vm.actions

sealed class HardwarePreferenceAction {
    data class SetHardwareButtonControl(val enabled: Boolean) : HardwarePreferenceAction()
    data class SetSoundsOn(val enabled: Boolean) : HardwarePreferenceAction()
    data class SetVibrationOn(val enabled: Boolean) : HardwarePreferenceAction()
    data class SetShowLabels(val show: Boolean) : HardwarePreferenceAction()
}
