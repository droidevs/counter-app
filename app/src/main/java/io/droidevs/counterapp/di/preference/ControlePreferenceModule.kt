package io.droidevs.counterapp.di.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.data.preference.impl.controle.HardwareButtonControlPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.controle.LabelControlPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.controle.SoundsOnPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.controle.VibrationOnPreferenceImpl
import io.droidevs.counterapp.domain.preference.controle.HardwareButtonControlPreference
import io.droidevs.counterapp.domain.preference.controle.LabelControlPreference
import io.droidevs.counterapp.domain.preference.controle.SoundsOnPreference
import io.droidevs.counterapp.domain.preference.controle.VibrationOnPreference
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ControlePreferenceModule {

    @Provides
    @Singleton
    fun provideHardwareButtonControlPreference(dataStore: DataStore<Preferences>): HardwareButtonControlPreference =
        HardwareButtonControlPreferenceImpl(dataStore)

    @Provides
    @Singleton
    fun provideLabelControlPreference(dataStore: DataStore<Preferences>): LabelControlPreference =
        LabelControlPreferenceImpl(dataStore)

    @Provides
    @Singleton
    fun provideSoundsOnPreference(dataStore: DataStore<Preferences>): SoundsOnPreference =
        SoundsOnPreferenceImpl(dataStore)

    @Provides
    @Singleton
    fun provideVibrationOnPreference(dataStore: DataStore<Preferences>): VibrationOnPreference =
        VibrationOnPreferenceImpl(dataStore)
}
