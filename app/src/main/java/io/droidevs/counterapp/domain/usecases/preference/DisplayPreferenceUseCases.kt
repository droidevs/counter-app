package io.droidevs.counterapp.domain.usecases.preference

import io.droidevs.counterapp.domain.usecases.preference.display.GetThemeUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.SetThemeUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.GetHideControlsUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.SetHideControlsUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.GetHideLastUpdateUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.SetHideLastUpdateUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.GetHideCounterCategoryLabelUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.SetHideCounterCategoryLabelUseCase

data class DisplayPreferenceUseCases(
    val getTheme: GetThemeUseCase,
    val setTheme: SetThemeUseCase,
    val getHideControls: GetHideControlsUseCase,
    val setHideControls: SetHideControlsUseCase,
    val getHideLastUpdate: GetHideLastUpdateUseCase,
    val setHideLastUpdate: SetHideLastUpdateUseCase,
    val getHideCounterCategoryLabel: GetHideCounterCategoryLabelUseCase,
    val setHideCounterCategoryLabel: SetHideCounterCategoryLabelUseCase
)
