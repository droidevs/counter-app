package io.droidevs.counterapp.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
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
    fun provideGetThemeUseCase(pref: ThemePreference, dispatchers: DispatcherProvider): GetThemeUseCase = GetThemeUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideSetThemeUseCase(pref: ThemePreference, dispatchers: DispatcherProvider): SetThemeUseCase = SetThemeUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideGetHideControlsUseCase(pref: HideControlsPreference, dispatchers: DispatcherProvider): GetHideControlsUseCase = GetHideControlsUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideSetHideControlsUseCase(pref: HideControlsPreference, dispatchers: DispatcherProvider): SetHideControlsUseCase = SetHideControlsUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideGetHideLastUpdateUseCase(pref: HideLastUpdatePreference, dispatchers: DispatcherProvider): GetHideLastUpdateUseCase = GetHideLastUpdateUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideSetHideLastUpdateUseCase(pref: HideLastUpdatePreference, dispatchers: DispatcherProvider): SetHideLastUpdateUseCase = SetHideLastUpdateUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideGetKeepScreenOnUseCase(pref: KeepScreenOnPreference, dispatchers: DispatcherProvider): GetKeepScreenOnUseCase = GetKeepScreenOnUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideSetKeepScreenOnUseCase(pref: KeepScreenOnPreference, dispatchers: DispatcherProvider): SetKeepScreenOnUseCase = SetKeepScreenOnUseCase(pref, dispatchers)

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
