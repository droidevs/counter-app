package io.droidevs.counterapp.di.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.BuildConfig
import io.droidevs.counterapp.data.preference.impl.notification.CounterLimitNotificationPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.notification.DailySummaryNotificationPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.notification.NotificationSoundPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.notification.NotificationVibrationPatternPreferenceImpl
import io.droidevs.counterapp.domain.preference.notification.CounterLimitNotificationPreference
import io.droidevs.counterapp.domain.preference.notification.DailySummaryNotificationPreference
import io.droidevs.counterapp.domain.preference.notification.NotificationSoundPreference
import io.droidevs.counterapp.domain.preference.notification.NotificationVibrationPatternPreference
import io.droidevs.counterapp.preference.notification.DummyCounterLimitNotificationPreference
import io.droidevs.counterapp.preference.notification.DummyDailySummaryNotificationPreference
import io.droidevs.counterapp.preference.notification.DummyNotificationSoundPreference
import io.droidevs.counterapp.preference.notification.DummyNotificationVibrationPatternPreference
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationPreferenceModule {

    @Provides
    @Singleton
    fun provideCounterLimitNotificationPreference(dataStore: Lazy<DataStore<Preferences>>): CounterLimitNotificationPreference = 
        if (BuildConfig.DEBUG) {
            DummyCounterLimitNotificationPreference()
        } else {
            CounterLimitNotificationPreferenceImpl(dataStore.get())
        }

    @Provides
    @Singleton
    fun provideDailySummaryNotificationPreference(dataStore: Lazy<DataStore<Preferences>>): DailySummaryNotificationPreference = 
        if (BuildConfig.DEBUG) {
            DummyDailySummaryNotificationPreference()
        } else {
            DailySummaryNotificationPreferenceImpl(dataStore.get())
        }

    @Provides
    @Singleton
    fun provideNotificationSoundPreference(dataStore: Lazy<DataStore<Preferences>>): NotificationSoundPreference = 
        if (BuildConfig.DEBUG) {
            DummyNotificationSoundPreference()
        } else {
            NotificationSoundPreferenceImpl(dataStore.get())
        }

    @Provides
    @Singleton
    fun provideNotificationVibrationPatternPreference(dataStore: Lazy<DataStore<Preferences>>): NotificationVibrationPatternPreference = 
        if (BuildConfig.DEBUG) {
            DummyNotificationVibrationPatternPreference()
        } else {
            NotificationVibrationPatternPreferenceImpl(dataStore.get())
        }
}
