package io.droidevs.counterapp.domain.preference

import io.droidevs.counterapp.domain.preference.controle.LabelControlPreference
import io.droidevs.counterapp.domain.preference.display.HideControlsPreference
import io.droidevs.counterapp.domain.preference.display.HideLastUpdatePreference
import io.droidevs.counterapp.domain.preference.display.KeepScreenOnPreference
import io.droidevs.counterapp.domain.preference.display.ThemePreference

data class UiAppearancePreferences(
    val theme: ThemePreference,
    val hideControls: HideControlsPreference,
    val hideLastUpdate: HideLastUpdatePreference,
    val keepScreenOn: KeepScreenOnPreference,
    val labelControl: LabelControlPreference     // show/hide labels
)