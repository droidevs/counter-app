package io.droidevs.counterapp.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.notification.CounterLimitNotificationPreference
import io.droidevs.counterapp.domain.preference.notification.DailySummaryNotificationPreference
import io.droidevs.counterapp.domain.preference.notification.NotificationSoundPreference
import io.droidevs.counterapp.domain.preference.notification.NotificationVibrationPatternPreference
import io.droidevs.counterapp.domain.usecases.preference.NotificationPreferenceUseCases
import io.droidevs.counterapp.domain.usecases.preference.notification.GetCounterLimitNotificationUseCase
import io.droidevs.counterapp.domain.usecases.preference.notification.GetDailySummaryNotificationUseCase
import io.droidevs.counterapp.domain.usecases.preference.notification.GetNotificationSoundUseCase
import io.droidevs.counterapp.domain.usecases.preference.notification.GetNotificationVibrationPatternUseCase
import io.droidevs.counterapp.domain.usecases.preference.notification.SetCounterLimitNotificationUseCase
import io.droidevs.counterapp.domain.usecases.preference.notification.SetDailySummaryNotificationUseCase
import io.droidevs.counterapp.domain.usecases.preference.notification.SetNotificationSoundUseCase
import io.droidevs.counterapp.domain.usecases.preference.notification.SetNotificationVibrationPatternUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationUseCaseModule {

    @Provides
    @Singleton
    fun provideGetCounterLimitNotificationUseCase(pref: CounterLimitNotificationPreference, dispatchers: DispatcherProvider): GetCounterLimitNotificationUseCase = GetCounterLimitNotificationUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideSetCounterLimitNotificationUseCase(pref: CounterLimitNotificationPreference, dispatchers: DispatcherProvider): SetCounterLimitNotificationUseCase = SetCounterLimitNotificationUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideGetDailySummaryNotificationUseCase(pref: DailySummaryNotificationPreference, dispatchers: DispatcherProvider): GetDailySummaryNotificationUseCase = GetDailySummaryNotificationUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideSetDailySummaryNotificationUseCase(pref: DailySummaryNotificationPreference, dispatchers: DispatcherProvider): SetDailySummaryNotificationUseCase = SetDailySummaryNotificationUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideGetNotificationSoundUseCase(pref: NotificationSoundPreference, dispatchers: DispatcherProvider): GetNotificationSoundUseCase = GetNotificationSoundUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideSetNotificationSoundUseCase(pref: NotificationSoundPreference, dispatchers: DispatcherProvider): SetNotificationSoundUseCase = SetNotificationSoundUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideGetNotificationVibrationPatternUseCase(pref: NotificationVibrationPatternPreference, dispatchers: DispatcherProvider): GetNotificationVibrationPatternUseCase = GetNotificationVibrationPatternUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideSetNotificationVibrationPatternUseCase(pref: NotificationVibrationPatternPreference, dispatchers: DispatcherProvider): SetNotificationVibrationPatternUseCase = SetNotificationVibrationPatternUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideNotificationPreferenceUseCases(
        getCounterLimitNotificationUseCase: GetCounterLimitNotificationUseCase,
        setCounterLimitNotificationUseCase: SetCounterLimitNotificationUseCase,
        getDailySummaryNotificationUseCase: GetDailySummaryNotificationUseCase,
        setDailySummaryNotificationUseCase: SetDailySummaryNotificationUseCase,
        getNotificationSoundUseCase: GetNotificationSoundUseCase,
        setNotificationSoundUseCase: SetNotificationSoundUseCase,
        getNotificationVibrationPatternUseCase: GetNotificationVibrationPatternUseCase,
        setNotificationVibrationPatternUseCase: SetNotificationVibrationPatternUseCase
    ): NotificationPreferenceUseCases = NotificationPreferenceUseCases(
        getCounterLimitNotification = getCounterLimitNotificationUseCase,
        setCounterLimitNotification = setCounterLimitNotificationUseCase,
        getDailySummaryNotification = getDailySummaryNotificationUseCase,
        setDailySummaryNotification = setDailySummaryNotificationUseCase,
        getNotificationSound = getNotificationSoundUseCase,
        setNotificationSound = setNotificationSoundUseCase,
        getNotificationVibrationPattern = getNotificationVibrationPatternUseCase,
        setNotificationVibrationPattern = setNotificationVibrationPatternUseCase
    )
}
