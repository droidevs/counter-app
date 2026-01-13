package io.droidevs.counterapp.di.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.BuildConfig
import io.droidevs.counterapp.data.preference.impl.display.HideControlsPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.display.HideLastUpdatePreferenceImpl
import io.droidevs.counterapp.data.preference.impl.display.KeepScreenOnPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.display.ThemePreferenceImpl
import io.droidevs.counterapp.domain.preference.display.HideControlsPreference
import io.droidevs.counterapp.domain.preference.display.HideLastUpdatePreference
import io.droidevs.counterapp.domain.preference.display.KeepScreenOnPreference
import io.droidevs.counterapp.domain.preference.display.ThemePreference
import io.droidevs.counterapp.preference.display.DummyHideControlsPreference
import io.droidevs.counterapp.preference.display.DummyHideLastUpdatePreference
import io.droidevs.counterapp.preference.display.DummyKeepScreenOnPreference
import io.droidevs.counterapp.preference.display.DummyThemePreference
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DisplayPreferenceModule {

    @Provides
    @Singleton
    fun provideHideControlsPreference(dataStore: Lazy<DataStore<Preferences>>): HideControlsPreference = 
        if (BuildConfig.DEBUG) {
            DummyHideControlsPreference()
        } else {
            HideControlsPreferenceImpl(dataStore.get())
        }

    @Provides
    @Singleton
    fun provideHideLastUpdatePreference(dataStore: Lazy<DataStore<Preferences>>): HideLastUpdatePreference = 
        if (BuildConfig.DEBUG) {
            DummyHideLastUpdatePreference()
        } else {
            HideLastUpdatePreferenceImpl(dataStore.get())
        }

    @Provides
    @Singleton
    fun provideKeepScreenOnPreference(dataStore: Lazy<DataStore<Preferences>>): KeepScreenOnPreference = 
        if (BuildConfig.DEBUG) {
            DummyKeepScreenOnPreference()
        } else {
            KeepScreenOnPreferenceImpl(dataStore.get())
        }

    @Provides
    @Singleton
    fun provideThemePreference(dataStore: Lazy<DataStore<Preferences>>): ThemePreference = 
        if (BuildConfig.DEBUG) {
            DummyThemePreference()
        } else {
            ThemePreferenceImpl(dataStore.get())
        }
}
