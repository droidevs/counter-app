package io.droidevs.counterapp.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.data.preference.impl.backup.AutoBackupPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.backup.BackupIntervalPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.backup.BackupLocationPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.controle.HardwareButtonControlPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.controle.LabelControlPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.controle.SoundsOnPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.controle.VibrationOnPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.counter.CounterIncrementStepPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.counter.DefaultCounterValuePreferenceImpl
import io.droidevs.counterapp.data.preference.impl.counter.MaximumCounterValuePreferenceImpl
import io.droidevs.counterapp.data.preference.impl.counter.MinimumCounterValuePreferenceImpl
import io.droidevs.counterapp.data.preference.impl.display.HideControlsPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.display.HideLastUpdatePreferenceImpl
import io.droidevs.counterapp.data.preference.impl.display.KeepScreenOnPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.display.ThemePreferenceImpl
import io.droidevs.counterapp.data.preference.impl.notification.CounterLimitNotificationPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.notification.DailySummaryNotificationPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.notification.NotificationSoundPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.notification.NotificationVibrationPatternPreferenceImpl
import io.droidevs.counterapp.domain.preference.buckup.AutoBackupPreference
import io.droidevs.counterapp.domain.preference.buckup.BackupIntervalPreference
import io.droidevs.counterapp.domain.preference.buckup.BackupLocationPreference
import io.droidevs.counterapp.domain.preference.controle.HardwareButtonControlPreference
import io.droidevs.counterapp.domain.preference.controle.LabelControlPreference
import io.droidevs.counterapp.domain.preference.controle.SoundsOnPreference
import io.droidevs.counterapp.domain.preference.controle.VibrationOnPreference
import io.droidevs.counterapp.domain.preference.counter.CounterIncrementStepPreference
import io.droidevs.counterapp.domain.preference.counter.DefaultCounterValuePreference
import io.droidevs.counterapp.domain.preference.counter.MaximumCounterValuePreference
import io.droidevs.counterapp.domain.preference.counter.MinimumCounterValuePreference
import io.droidevs.counterapp.domain.preference.display.HideControlsPreference
import io.droidevs.counterapp.domain.preference.display.HideLastUpdatePreference
import io.droidevs.counterapp.domain.preference.display.KeepScreenOnPreference
import io.droidevs.counterapp.domain.preference.display.ThemePreference
import io.droidevs.counterapp.domain.preference.notification.CounterLimitNotificationPreference
import io.droidevs.counterapp.domain.preference.notification.DailySummaryNotificationPreference
import io.droidevs.counterapp.domain.preference.notification.NotificationSoundPreference
import io.droidevs.counterapp.domain.preference.notification.NotificationVibrationPatternPreference
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun provideAutoBackupPreference(dataStore: DataStore<Preferences>): AutoBackupPreference =
        AutoBackupPreferenceImpl(dataStore)

    @Provides
    @Singleton
    fun provideBackupIntervalPreference(dataStore: DataStore<Preferences>): BackupIntervalPreference =
        BackupIntervalPreferenceImpl(dataStore)

    @Provides
    @Singleton
    fun provideBackupLocationPreference(dataStore: DataStore<Preferences>): BackupLocationPreference =
        BackupLocationPreferenceImpl(dataStore)

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

    @Provides
    @Singleton
    fun provideCounterIncrementStepPreference(dataStore: DataStore<Preferences>): CounterIncrementStepPreference =
        CounterIncrementStepPreferenceImpl(dataStore)

    @Provides
    @Singleton
    fun provideDefaultCounterValuePreference(dataStore: DataStore<Preferences>): DefaultCounterValuePreference =
        DefaultCounterValuePreferenceImpl(dataStore)

    @Provides
    @Singleton
    fun provideMaximumCounterValuePreference(dataStore: DataStore<Preferences>): MaximumCounterValuePreference =
        MaximumCounterValuePreferenceImpl(dataStore)

    @Provides
    @Singleton
    fun provideMinimumCounterValuePreference(dataStore: DataStore<Preferences>): MinimumCounterValuePreference =
        MinimumCounterValuePreferenceImpl(dataStore)

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

    @Provides
    @Singleton
    fun provideCounterLimitNotificationPreference(dataStore: DataStore<Preferences>): CounterLimitNotificationPreference =
        CounterLimitNotificationPreferenceImpl(dataStore)

    @Provides
    @Singleton
    fun provideDailySummaryNotificationPreference(dataStore: DataStore<Preferences>): DailySummaryNotificationPreference =
        DailySummaryNotificationPreferenceImpl(dataStore)

    @Provides
    @Singleton
    fun provideNotificationSoundPreference(dataStore: DataStore<Preferences>): NotificationSoundPreference =
        NotificationSoundPreferenceImpl(dataStore)

    @Provides
    @Singleton
    fun provideNotificationVibrationPatternPreference(dataStore: DataStore<Preferences>): NotificationVibrationPatternPreference =
        NotificationVibrationPatternPreferenceImpl(dataStore)
}