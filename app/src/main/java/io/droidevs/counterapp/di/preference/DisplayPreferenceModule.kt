package io.droidevs.counterapp.di.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.data.preference.impl.display.HideControlsPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.display.HideLastUpdatePreferenceImpl
import io.droidevs.counterapp.data.preference.impl.display.KeepScreenOnPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.display.ThemePreferenceImpl
import io.droidevs.counterapp.domain.preference.display.HideControlsPreference
import io.droidevs.counterapp.domain.preference.display.HideLastUpdatePreference
import io.droidevs.counterapp.domain.preference.display.KeepScreenOnPreference
import io.droidevs.counterapp.domain.preference.display.ThemePreference
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DisplayPreferenceModule {

    @Provides
    @Singleton
    fun provideHideControlsPreference(dataStore: DataStore<Preferences>): HideControlsPreference =
        HideControlsPreferenceImpl(dataStore)

    @Provides
    @Singleton
    fun provideHideLastUpdatePreference(dataStore: DataStore<Preferences>): HideLastUpdatePreference =
        HideLastUpdatePreferenceImpl(dataStore)

    @Provides
    @Singleton
    fun provideKeepScreenOnPreference(dataStore: DataStore<Preferences>): KeepScreenOnPreference =
        KeepScreenOnPreferenceImpl(dataStore)

    @Provides
    @Singleton
    fun provideThemePreference(dataStore: DataStore<Preferences>): ThemePreference =
        ThemePreferenceImpl(dataStore)
}
