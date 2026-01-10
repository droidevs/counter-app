package io.droidevs.counterapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
import io.droidevs.counterapp.preference.backup.DummyAutoBackupPreference
import io.droidevs.counterapp.preference.backup.DummyBackupIntervalPreference
import io.droidevs.counterapp.preference.backup.DummyBackupLocationPreference
import io.droidevs.counterapp.preference.controle.DummyHardwareButtonControlPreference
import io.droidevs.counterapp.preference.controle.DummyLabelControlPreference
import io.droidevs.counterapp.preference.controle.DummySoundsOnPreference
import io.droidevs.counterapp.preference.controle.DummyVibrationOnPreference
import io.droidevs.counterapp.preference.counter.DummyCounterIncrementStepPreference
import io.droidevs.counterapp.preference.counter.DummyDefaultCounterValuePreference
import io.droidevs.counterapp.preference.counter.DummyMaximumCounterValuePreference
import io.droidevs.counterapp.preference.counter.DummyMinimumCounterValuePreference
import io.droidevs.counterapp.preference.display.DummyHideControlsPreference
import io.droidevs.counterapp.preference.display.DummyHideLastUpdatePreference
import io.droidevs.counterapp.preference.display.DummyKeepScreenOnPreference
import io.droidevs.counterapp.preference.display.DummyThemePreference
import io.droidevs.counterapp.preference.notification.DummyCounterLimitNotificationPreference
import io.droidevs.counterapp.preference.notification.DummyDailySummaryNotificationPreference
import io.droidevs.counterapp.preference.notification.DummyNotificationSoundPreference
import io.droidevs.counterapp.preference.notification.DummyNotificationVibrationPatternPreference
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DummyPreferencesModule {

    @Provides
    @Singleton
    fun provideAutoBackupPreference(): AutoBackupPreference =
        DummyAutoBackupPreference()

    @Provides
    @Singleton
    fun provideBackupIntervalPreference(): BackupIntervalPreference =
        DummyBackupIntervalPreference()

    @Provides
    @Singleton
    fun provideBackupLocationPreference(): BackupLocationPreference =
        DummyBackupLocationPreference()

    @Provides
    @Singleton
    fun provideHardwareButtonControlPreference(): HardwareButtonControlPreference =
        DummyHardwareButtonControlPreference()

    @Provides
    @Singleton
    fun provideLabelControlPreference(): LabelControlPreference =
        DummyLabelControlPreference()

    @Provides
    @Singleton
    fun provideSoundsOnPreference(): SoundsOnPreference =
        DummySoundsOnPreference()

    @Provides
    @Singleton
    fun provideVibrationOnPreference(): VibrationOnPreference =
        DummyVibrationOnPreference()

    @Provides
    @Singleton
    fun provideCounterIncrementStepPreference(): CounterIncrementStepPreference =
        DummyCounterIncrementStepPreference()

    @Provides
    @Singleton
    fun provideDefaultCounterValuePreference(): DefaultCounterValuePreference =
        DummyDefaultCounterValuePreference()

    @Provides
    @Singleton
    fun provideMaximumCounterValuePreference(): MaximumCounterValuePreference =
        DummyMaximumCounterValuePreference()

    @Provides
    @Singleton
    fun provideMinimumCounterValuePreference(): MinimumCounterValuePreference =
        DummyMinimumCounterValuePreference()

    @Provides
    @Singleton
    fun provideHideControlsPreference(): HideControlsPreference =
        DummyHideControlsPreference()

    @Provides
    @Singleton
    fun provideHideLastUpdatePreference(): HideLastUpdatePreference =
        DummyHideLastUpdatePreference()

    @Provides
    @Singleton
    fun provideKeepScreenOnPreference(): KeepScreenOnPreference =
        DummyKeepScreenOnPreference()

    @Provides
    @Singleton
    fun provideThemePreference(): ThemePreference =
        DummyThemePreference()

    @Provides
    @Singleton
    fun provideCounterLimitNotificationPreference(): CounterLimitNotificationPreference =
        DummyCounterLimitNotificationPreference()

    @Provides
    @Singleton
    fun provideDailySummaryNotificationPreference(): DailySummaryNotificationPreference =
        DummyDailySummaryNotificationPreference()

    @Provides
    @Singleton
    fun provideNotificationSoundPreference(): NotificationSoundPreference =
        DummyNotificationSoundPreference()

    @Provides
    @Singleton
    fun provideNotificationVibrationPatternPreference(): NotificationVibrationPatternPreference =
        DummyNotificationVibrationPatternPreference()
}