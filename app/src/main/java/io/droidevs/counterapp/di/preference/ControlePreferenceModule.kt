package io.droidevs.counterapp.di.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.BuildConfig
import io.droidevs.counterapp.data.preference.impl.controle.HardwareButtonControlPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.controle.LabelControlPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.controle.SoundsOnPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.controle.VibrationOnPreferenceImpl
import io.droidevs.counterapp.domain.preference.controle.HardwareButtonControlPreference
import io.droidevs.counterapp.domain.preference.controle.LabelControlPreference
import io.droidevs.counterapp.domain.preference.controle.SoundsOnPreference
import io.droidevs.counterapp.domain.preference.controle.VibrationOnPreference
import io.droidevs.counterapp.preference.controle.DummyHardwareButtonControlPreference
import io.droidevs.counterapp.preference.controle.DummyLabelControlPreference
import io.droidevs.counterapp.preference.controle.DummySoundsOnPreference
import io.droidevs.counterapp.preference.controle.DummyVibrationOnPreference
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ControlePreferenceModule {

    @Provides
    @Singleton
    fun provideHardwareButtonControlPreference(dataStore: Lazy<DataStore<Preferences>>): HardwareButtonControlPreference = 
        if (BuildConfig.DEBUG) {
            DummyHardwareButtonControlPreference()
        } else {
            HardwareButtonControlPreferenceImpl(dataStore.get())
        }

    @Provides
    @Singleton
    fun provideLabelControlPreference(dataStore: Lazy<DataStore<Preferences>>): LabelControlPreference = 
        if (BuildConfig.DEBUG) {
            DummyLabelControlPreference()
        } else {
            LabelControlPreferenceImpl(dataStore.get())
        }

    @Provides
    @Singleton
    fun provideSoundsOnPreference(dataStore: Lazy<DataStore<Preferences>>): SoundsOnPreference = 
        if (BuildConfig.DEBUG) {
            DummySoundsOnPreference()
        } else {
            SoundsOnPreferenceImpl(dataStore.get())
        }

    @Provides
    @Singleton
    fun provideVibrationOnPreference(dataStore: Lazy<DataStore<Preferences>>): VibrationOnPreference = 
        if (BuildConfig.DEBUG) {
            DummyVibrationOnPreference()
        } else {
            VibrationOnPreferenceImpl(dataStore.get())
        }
}
