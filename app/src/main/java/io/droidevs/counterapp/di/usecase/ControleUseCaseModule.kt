package io.droidevs.counterapp.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.controle.HardwareButtonControlPreference
import io.droidevs.counterapp.domain.preference.controle.SoundsOnPreference
import io.droidevs.counterapp.domain.preference.controle.VibrationOnPreference
import io.droidevs.counterapp.domain.preference.display.KeepScreenOnPreference
import io.droidevs.counterapp.domain.usecases.preference.HardwarePreferenceUseCases
import io.droidevs.counterapp.domain.usecases.preference.controle.GetHardwareButtonControlUseCase
import io.droidevs.counterapp.domain.usecases.preference.controle.GetSoundsOnUseCase
import io.droidevs.counterapp.domain.usecases.preference.controle.GetVibrationOnUseCase
import io.droidevs.counterapp.domain.usecases.preference.controle.GetKeepScreenOnUseCase
import io.droidevs.counterapp.domain.usecases.preference.controle.SetHardwareButtonControlUseCase
import io.droidevs.counterapp.domain.usecases.preference.controle.SetSoundsOnUseCase
import io.droidevs.counterapp.domain.usecases.preference.controle.SetVibrationOnUseCase
import io.droidevs.counterapp.domain.usecases.preference.controle.SetKeepScreenOnUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ControleUseCaseModule {

    @Provides
    @Singleton
    fun provideGetHardwareButtonControlUseCase(pref: HardwareButtonControlPreference, dispatchers: DispatcherProvider): GetHardwareButtonControlUseCase =
        GetHardwareButtonControlUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideSetHardwareButtonControlUseCase(pref: HardwareButtonControlPreference, dispatchers: DispatcherProvider): SetHardwareButtonControlUseCase =
        SetHardwareButtonControlUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideGetSoundsOnUseCase(pref: SoundsOnPreference, dispatchers: DispatcherProvider): GetSoundsOnUseCase =
        GetSoundsOnUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideSetSoundsOnUseCase(pref: SoundsOnPreference, dispatchers: DispatcherProvider): SetSoundsOnUseCase =
        SetSoundsOnUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideGetVibrationOnUseCase(pref: VibrationOnPreference, dispatchers: DispatcherProvider): GetVibrationOnUseCase =
        GetVibrationOnUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideSetVibrationOnUseCase(pref: VibrationOnPreference, dispatchers: DispatcherProvider): SetVibrationOnUseCase =
        SetVibrationOnUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideGetKeepScreenOnUseCase(pref: KeepScreenOnPreference, dispatchers: DispatcherProvider): GetKeepScreenOnUseCase =
        GetKeepScreenOnUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideSetKeepScreenOnUseCase(pref: KeepScreenOnPreference, dispatchers: DispatcherProvider): SetKeepScreenOnUseCase =
        SetKeepScreenOnUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideHardwarePreferenceUseCases(
        getHardwareButtonControl: GetHardwareButtonControlUseCase,
        setHardwareButtonControl: SetHardwareButtonControlUseCase,
        getSoundsOn: GetSoundsOnUseCase,
        setSoundsOn: SetSoundsOnUseCase,
        getVibrationOn: GetVibrationOnUseCase,
        setVibrationOn: SetVibrationOnUseCase,
        getKeepScreenOn: GetKeepScreenOnUseCase,
        setKeepScreenOn: SetKeepScreenOnUseCase,
    ): HardwarePreferenceUseCases = HardwarePreferenceUseCases(
        getHardwareButtonControl = getHardwareButtonControl,
        setHardwareButtonControl = setHardwareButtonControl,
        getSoundsOn = getSoundsOn,
        setSoundsOn = setSoundsOn,
        getVibrationOn = getVibrationOn,
        setVibrationOn = setVibrationOn,
        getKeepScreenOn = getKeepScreenOn,
        setKeepScreenOn = setKeepScreenOn,
    )
}
