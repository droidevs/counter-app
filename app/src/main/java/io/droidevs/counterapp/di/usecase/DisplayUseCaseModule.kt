package io.droidevs.counterapp.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.domain.preference.display.HideControlsPreference
import io.droidevs.counterapp.domain.preference.display.HideLastUpdatePreference
import io.droidevs.counterapp.domain.preference.display.KeepScreenOnPreference
import io.droidevs.counterapp.domain.preference.display.ThemePreference
import io.droidevs.counterapp.domain.usecases.preference.DisplayPreferenceUseCases
import io.droidevs.counterapp.domain.usecases.preference.display.GetThemeUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.SetThemeUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.GetHideControlsUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.SetHideControlsUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.GetHideLastUpdateUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.SetHideLastUpdateUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.GetKeepScreenOnUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.SetKeepScreenOnUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DisplayUseCaseModule {

    @Provides
    @Singleton
    fun provideGetThemeUseCase(pref: ThemePreference): GetThemeUseCase = GetThemeUseCase(pref)

    @Provides
    @Singleton
    fun provideSetThemeUseCase(pref: ThemePreference): SetThemeUseCase = SetThemeUseCase(pref)

    @Provides
    @Singleton
    fun provideGetHideControlsUseCase(pref: HideControlsPreference): GetHideControlsUseCase = GetHideControlsUseCase(pref)

    @Provides
    @Singleton
    fun provideSetHideControlsUseCase(pref: HideControlsPreference): SetHideControlsUseCase = SetHideControlsUseCase(pref)

    @Provides
    @Singleton
    fun provideGetHideLastUpdateUseCase(pref: HideLastUpdatePreference): GetHideLastUpdateUseCase = GetHideLastUpdateUseCase(pref)

    @Provides
    @Singleton
    fun provideSetHideLastUpdateUseCase(pref: HideLastUpdatePreference): SetHideLastUpdateUseCase = SetHideLastUpdateUseCase(pref)

    @Provides
    @Singleton
    fun provideGetKeepScreenOnUseCase(pref: KeepScreenOnPreference): GetKeepScreenOnUseCase = GetKeepScreenOnUseCase(pref)

    @Provides
    @Singleton
    fun provideSetKeepScreenOnUseCase(pref: KeepScreenOnPreference): SetKeepScreenOnUseCase = SetKeepScreenOnUseCase(pref)

    @Provides
    @Singleton
    fun provideDisplayPreferenceUseCases(
        getThemeUseCase: GetThemeUseCase,
        setThemeUseCase: SetThemeUseCase,
        getHideControlsUseCase: GetHideControlsUseCase,
        setHideControlsUseCase: SetHideControlsUseCase,
        getHideLastUpdateUseCase: GetHideLastUpdateUseCase,
        setHideLastUpdateUseCase: SetHideLastUpdateUseCase,
        getKeepScreenOnUseCase: GetKeepScreenOnUseCase,
        setKeepScreenOnUseCase: SetKeepScreenOnUseCase
    ): DisplayPreferenceUseCases = DisplayPreferenceUseCases(
        getTheme = getThemeUseCase,
        setTheme = setThemeUseCase,
        getHideControls = getHideControlsUseCase,
        setHideControls = setHideControlsUseCase,
        getHideLastUpdate = getHideLastUpdateUseCase,
        setHideLastUpdate = setHideLastUpdateUseCase,
        getKeepScreenOn = getKeepScreenOnUseCase,
        setKeepScreenOn = setKeepScreenOnUseCase
    )
}
