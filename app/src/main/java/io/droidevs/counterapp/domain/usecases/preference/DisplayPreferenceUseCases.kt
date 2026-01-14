package io.droidevs.counterapp.domain.usecases.preference

import io.droidevs.counterapp.domain.usecases.preference.display.GetThemeUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.SetThemeUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.GetHideControlsUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.SetHideControlsUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.GetHideLastUpdateUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.SetHideLastUpdateUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.GetKeepScreenOnUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.SetKeepScreenOnUseCase

data class DisplayPreferenceUseCases(
    val getTheme: GetThemeUseCase,
    val setTheme: SetThemeUseCase,
    val getHideControls: GetHideControlsUseCase,
    val setHideControls: SetHideControlsUseCase,
    val getHideLastUpdate: GetHideLastUpdateUseCase,
    val setHideLastUpdate: SetHideLastUpdateUseCase,
    val getKeepScreenOn: GetKeepScreenOnUseCase,
    val setKeepScreenOn: SetKeepScreenOnUseCase
)
