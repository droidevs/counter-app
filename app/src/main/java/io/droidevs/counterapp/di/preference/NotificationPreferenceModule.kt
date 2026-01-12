package io.droidevs.counterapp.di.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.data.preference.impl.notification.CounterLimitNotificationPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.notification.DailySummaryNotificationPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.notification.NotificationSoundPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.notification.NotificationVibrationPatternPreferenceImpl
import io.droidevs.counterapp.domain.preference.notification.CounterLimitNotificationPreference
import io.droidevs.counterapp.domain.preference.notification.DailySummaryNotificationPreference
import io.droidevs.counterapp.domain.preference.notification.NotificationSoundPreference
import io.droidevs.counterapp.domain.preference.notification.NotificationVibrationPatternPreference
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationPreferenceModule {

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
