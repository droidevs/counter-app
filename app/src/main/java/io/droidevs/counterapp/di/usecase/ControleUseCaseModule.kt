package io.droidevs.counterapp.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    fun provideGetHardwareButtonControlUseCase(pref: HardwareButtonControlPreference): GetHardwareButtonControlUseCase =
        GetHardwareButtonControlUseCase(pref)

    @Provides
    @Singleton
    fun provideSetHardwareButtonControlUseCase(pref: HardwareButtonControlPreference): SetHardwareButtonControlUseCase =
        SetHardwareButtonControlUseCase(pref)

    @Provides
    @Singleton
    fun provideGetSoundsOnUseCase(pref: SoundsOnPreference): GetSoundsOnUseCase =
        GetSoundsOnUseCase(pref)

    @Provides
    @Singleton
    fun provideSetSoundsOnUseCase(pref: SoundsOnPreference): SetSoundsOnUseCase =
        SetSoundsOnUseCase(pref)

    @Provides
    @Singleton
    fun provideGetVibrationOnUseCase(pref: VibrationOnPreference): GetVibrationOnUseCase =
        GetVibrationOnUseCase(pref)

    @Provides
    @Singleton
    fun provideSetVibrationOnUseCase(pref: VibrationOnPreference): SetVibrationOnUseCase =
        SetVibrationOnUseCase(pref)

    @Provides
    @Singleton
    fun provideGetLabelControlUseCase(pref: LabelControlPreference): GetLabelControlUseCase =
        GetLabelControlUseCase(pref)

    @Provides
    @Singleton
    fun provideSetLabelControlUseCase(pref: LabelControlPreference): SetLabelControlUseCase =
        SetLabelControlUseCase(pref)

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
