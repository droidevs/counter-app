package io.droidevs.counterapp.ui.vm.actions

import io.droidevs.counterapp.data.Theme

sealed class DisplayPreferenceAction {
    data class SetTheme(val theme: Theme) : DisplayPreferenceAction()
    data class SetHideControls(val hide: Boolean) : DisplayPreferenceAction()
    data class SetHideLastUpdate(val hide: Boolean) : DisplayPreferenceAction()
    data class SetKeepScreenOn(val keep: Boolean) : DisplayPreferenceAction()
}
