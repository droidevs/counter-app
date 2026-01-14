package io.droidevs.counterapp.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    fun provideGetCounterLimitNotificationUseCase(pref: CounterLimitNotificationPreference): GetCounterLimitNotificationUseCase = GetCounterLimitNotificationUseCase(pref)

    @Provides
    @Singleton
    fun provideSetCounterLimitNotificationUseCase(pref: CounterLimitNotificationPreference): SetCounterLimitNotificationUseCase = SetCounterLimitNotificationUseCase(pref)

    @Provides
    @Singleton
    fun provideGetDailySummaryNotificationUseCase(pref: DailySummaryNotificationPreference): GetDailySummaryNotificationUseCase = GetDailySummaryNotificationUseCase(pref)

    @Provides
    @Singleton
    fun provideSetDailySummaryNotificationUseCase(pref: DailySummaryNotificationPreference): SetDailySummaryNotificationUseCase = SetDailySummaryNotificationUseCase(pref)

    @Provides
    @Singleton
    fun provideGetNotificationSoundUseCase(pref: NotificationSoundPreference): GetNotificationSoundUseCase = GetNotificationSoundUseCase(pref)

    @Provides
    @Singleton
    fun provideSetNotificationSoundUseCase(pref: NotificationSoundPreference): SetNotificationSoundUseCase = SetNotificationSoundUseCase(pref)

    @Provides
    @Singleton
    fun provideGetNotificationVibrationPatternUseCase(pref: NotificationVibrationPatternPreference): GetNotificationVibrationPatternUseCase = GetNotificationVibrationPatternUseCase(pref)

    @Provides
    @Singleton
    fun provideSetNotificationVibrationPatternUseCase(pref: NotificationVibrationPatternPreference): SetNotificationVibrationPatternUseCase = SetNotificationVibrationPatternUseCase(pref)

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
