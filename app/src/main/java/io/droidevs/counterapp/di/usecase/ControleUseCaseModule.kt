package io.droidevs.counterapp.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.controle.HardwareButtonControlPreference
import io.droidevs.counterapp.domain.preference.controle.LabelControlPreference
import io.droidevs.counterapp.domain.preference.controle.SoundsOnPreference
import io.droidevs.counterapp.domain.preference.controle.VibrationOnPreference
import io.droidevs.counterapp.domain.usecases.preference.HardwarePreferenceUseCases
import io.droidevs.counterapp.domain.usecases.preference.controle.GetHardwareButtonControlUseCase
import io.droidevs.counterapp.domain.usecases.preference.controle.GetLabelControlUseCase
import io.droidevs.counterapp.domain.usecases.preference.controle.GetSoundsOnUseCase
import io.droidevs.counterapp.domain.usecases.preference.controle.GetVibrationOnUseCase
import io.droidevs.counterapp.domain.usecases.preference.controle.SetHardwareButtonControlUseCase
import io.droidevs.counterapp.domain.usecases.preference.controle.SetLabelControlUseCase
import io.droidevs.counterapp.domain.usecases.preference.controle.SetSoundsOnUseCase
import io.droidevs.counterapp.domain.usecases.preference.controle.SetVibrationOnUseCase
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
    fun provideGetLabelControlUseCase(pref: LabelControlPreference, dispatchers: DispatcherProvider): GetLabelControlUseCase =
        GetLabelControlUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideSetLabelControlUseCase(pref: LabelControlPreference, dispatchers: DispatcherProvider): SetLabelControlUseCase =
        SetLabelControlUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideHardwarePreferenceUseCases(
        getHardwareButtonControl: GetHardwareButtonControlUseCase,
        setHardwareButtonControl: SetHardwareButtonControlUseCase,
        getSoundsOn: GetSoundsOnUseCase,
        setSoundsOn: SetSoundsOnUseCase,
        getVibrationOn: GetVibrationOnUseCase,
        setVibrationOn: SetVibrationOnUseCase,
        getLabelControl: GetLabelControlUseCase,
        setLabelControl: SetLabelControlUseCase
    ): HardwarePreferenceUseCases = HardwarePreferenceUseCases(
        getHardwareButtonControl = getHardwareButtonControl,
        setHardwareButtonControl = setHardwareButtonControl,
        getSoundsOn = getSoundsOn,
        setSoundsOn = setSoundsOn,
        getVibrationOn = getVibrationOn,
        setVibrationOn = setVibrationOn,
        getLabelControl = getLabelControl,
        setLabelControl = setLabelControl
    )
}
