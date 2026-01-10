package io.droidevs.counterapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.preference.buckup.*
import io.droidevs.counterapp.domain.preference.controle.*
import io.droidevs.counterapp.domain.preference.counter.*
import io.droidevs.counterapp.domain.preference.display.*
import io.droidevs.counterapp.domain.preference.notification.*
import io.droidevs.counterapp.domain.usecases.preference.*
import io.droidevs.counterapp.domain.usecases.preference.buckup.*
import io.droidevs.counterapp.domain.usecases.preference.controle.*
import io.droidevs.counterapp.domain.usecases.preference.counter.*
import io.droidevs.counterapp.domain.usecases.preference.display.*
import io.droidevs.counterapp.domain.usecases.preference.notification.*
import io.droidevs.counterapp.domain.usecases.category.*
import io.droidevs.counterapp.domain.usecases.counters.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    // ===== Category Use Cases =====

    @Provides
    @Singleton
    fun provideCreateCategoryUseCase(repository: CategoryRepository): CreateCategoryUseCase =
        CreateCategoryUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteCategoryUseCase(repository: CategoryRepository): DeleteCategoryUseCase =
        DeleteCategoryUseCase(repository)

    @Provides
    @Singleton
    fun provideGetAllCategoriesUseCase(repository: CategoryRepository): GetAllCategoriesUseCase =
        GetAllCategoriesUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCategoryWithCountersUseCase(repository: CategoryRepository): GetCategoryWithCountersUseCase =
        GetCategoryWithCountersUseCase(repository)

    @Provides
    @Singleton
    fun provideGetExistingCategoryColorsUseCase(repository: CategoryRepository): GetExistingCategoryColorsUseCase =
        GetExistingCategoryColorsUseCase(repository)

    @Provides
    @Singleton
    fun provideGetSystemCategoriesUseCase(repository: CategoryRepository): GetSystemCategoriesUseCase =
        GetSystemCategoriesUseCase(repository)

    @Provides
    @Singleton
    fun provideGetTopCategoriesUseCase(repository: CategoryRepository): GetTopCategoriesUseCase =
        GetTopCategoriesUseCase(repository)

    @Provides
    @Singleton
    fun provideGetTotalCategoriesCountUseCase(repository: CategoryRepository): GetTotalCategoriesCountUseCase =
        GetTotalCategoriesCountUseCase(repository)

    @Provides
    @Singleton
    fun provideCategoryUseCases(
        createCategory: CreateCategoryUseCase,
        deleteCategory: DeleteCategoryUseCase,
        getAllCategories: GetAllCategoriesUseCase,
        getCategoryWithCounters: GetCategoryWithCountersUseCase,
        getExistingCategoryColors: GetExistingCategoryColorsUseCase,
        getSystemCategories: GetSystemCategoriesUseCase,
        getTopCategories: GetTopCategoriesUseCase,
        getTotalCategoriesCount: GetTotalCategoriesCountUseCase
    ): CategoryUseCases = CategoryUseCases(
        createCategory,
        deleteCategory,
        getAllCategories,
        getCategoryWithCounters,
        getExistingCategoryColors,
        getSystemCategories,
        getTopCategories,
        getTotalCategoriesCount
    )

    // ===== Counter Use Cases =====

    @Provides
    @Singleton
    fun provideCreateCounterUseCase(repository: CounterRepository): CreateCounterUseCase =
        CreateCounterUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteCounterUseCase(repository: CounterRepository): DeleteCounterUseCase =
        DeleteCounterUseCase(repository)

    @Provides
    @Singleton
    fun provideGetAllCountersUseCase(repository: CounterRepository): GetAllCountersUseCase =
        GetAllCountersUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCountersWithCategoriesUseCase(repository: CounterRepository): GetCountersWithCategoriesUseCase =
        GetCountersWithCategoriesUseCase(repository)

    @Provides
    @Singleton
    fun provideGetLimitCountersUseCase(repository: CounterRepository): GetLimitCountersUseCase =
        GetLimitCountersUseCase(repository)

    @Provides
    @Singleton
    fun provideGetLimitCountersWithCategoryUseCase(repository: CounterRepository): GetLimitCountersWithCategoryUseCase =
        GetLimitCountersWithCategoryUseCase(repository)

    @Provides
    @Singleton
    fun provideGetSystemCountersUseCase(repository: CounterRepository): GetSystemCountersUseCase =
        GetSystemCountersUseCase(repository)

    @Provides
    @Singleton
    fun provideGetTotalNumberOfCountersUseCase(repository: CounterRepository): GetTotalNumberOfCountersUseCase =
        GetTotalNumberOfCountersUseCase(repository)

    @Provides
    @Singleton
    fun provideIncrementSystemCounterUseCase(repository: CounterRepository): IncrementSystemCounterUseCase =
        IncrementSystemCounterUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateCounterUseCase(repository: CounterRepository): UpdateCounterUseCase =
        UpdateCounterUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateSystemCounterUseCase(repository: CounterRepository): UpdateSystemCounterUseCase =
        UpdateSystemCounterUseCase(repository)

    @Provides
    @Singleton
    fun provideCounterUseCases(
        createCounter: CreateCounterUseCase,
        deleteCounter: DeleteCounterUseCase,
        getAllCounters: GetAllCountersUseCase,
        getCountersWithCategories: GetCountersWithCategoriesUseCase,
        getLimitCounters: GetLimitCountersUseCase,
        getLimitCountersWithCategory: GetLimitCountersWithCategoryUseCase,
        getSystemCounters: GetSystemCountersUseCase,
        getTotalNumberOfCounters: GetTotalNumberOfCountersUseCase,
        incrementSystemCounter: IncrementSystemCounterUseCase,
        updateCounter: UpdateCounterUseCase,
        updateSystemCounter: UpdateSystemCounterUseCase
    ): CounterUseCases = CounterUseCases(
        createCounter,
        deleteCounter,
        getAllCounters,
        getCountersWithCategories,
        getLimitCounters,
        getLimitCountersWithCategory,
        getSystemCounters,
        getTotalNumberOfCounters,
        incrementSystemCounter,
        updateCounter,
        updateSystemCounter
    )

    // ===== Preference Use Cases =====

    @Provides
    @Singleton
    fun provideGetCounterIncrementStepUseCase(pref: io.droidevs.counterapp.domain.preference.counter.CounterIncrementStepPreference): GetCounterIncrementStepUseCase =
        GetCounterIncrementStepUseCase(pref)

    @Provides
    @Singleton
    fun provideSetCounterIncrementStepUseCase(pref: io.droidevs.counterapp.domain.preference.counter.CounterIncrementStepPreference): SetCounterIncrementStepUseCase =
        SetCounterIncrementStepUseCase(pref)

    @Provides
    @Singleton
    fun provideGetDefaultCounterValueUseCase(pref: io.droidevs.counterapp.domain.preference.counter.DefaultCounterValuePreference): GetDefaultCounterValueUseCase =
        GetDefaultCounterValueUseCase(pref)

    @Provides
    @Singleton
    fun provideSetDefaultCounterValueUseCase(pref: io.droidevs.counterapp.domain.preference.counter.DefaultCounterValuePreference): SetDefaultCounterValueUseCase =
        SetDefaultCounterValueUseCase(pref)

    @Provides
    @Singleton
    fun provideGetMinimumCounterValueUseCase(pref: io.droidevs.counterapp.domain.preference.counter.MinimumCounterValuePreference): GetMinimumCounterValueUseCase =
        GetMinimumCounterValueUseCase(pref)

    @Provides
    @Singleton
    fun provideSetMinimumCounterValueUseCase(pref: io.droidevs.counterapp.domain.preference.counter.MinimumCounterValuePreference): SetMinimumCounterValueUseCase =
        SetMinimumCounterValueUseCase(pref)

    @Provides
    @Singleton
    fun provideGetMaximumCounterValueUseCase(pref: io.droidevs.counterapp.domain.preference.counter.MaximumCounterValuePreference): GetMaximumCounterValueUseCase =
        GetMaximumCounterValueUseCase(pref)

    @Provides
    @Singleton
    fun provideSetMaximumCounterValueUseCase(pref: io.droidevs.counterapp.domain.preference.counter.MaximumCounterValuePreference): SetMaximumCounterValueUseCase =
        SetMaximumCounterValueUseCase(pref)

    // controle
    @Provides
    @Singleton
    fun provideGetHardwareButtonControlUseCase(pref: io.droidevs.counterapp.domain.preference.controle.HardwareButtonControlPreference): GetHardwareButtonControlUseCase =
        GetHardwareButtonControlUseCase(pref)

    @Provides
    @Singleton
    fun provideSetHardwareButtonControlUseCase(pref: io.droidevs.counterapp.domain.preference.controle.HardwareButtonControlPreference): SetHardwareButtonControlUseCase =
        SetHardwareButtonControlUseCase(pref)

    @Provides
    @Singleton
    fun provideGetSoundsOnUseCase(pref: io.droidevs.counterapp.domain.preference.controle.SoundsOnPreference): GetSoundsOnUseCase =
        GetSoundsOnUseCase(pref)

    @Provides
    @Singleton
    fun provideSetSoundsOnUseCase(pref: io.droidevs.counterapp.domain.preference.controle.SoundsOnPreference): SetSoundsOnUseCase =
        SetSoundsOnUseCase(pref)

    @Provides
    @Singleton
    fun provideGetVibrationOnUseCase(pref: io.droidevs.counterapp.domain.preference.controle.VibrationOnPreference): GetVibrationOnUseCase =
        GetVibrationOnUseCase(pref)

    @Provides
    @Singleton
    fun provideSetVibrationOnUseCase(pref: io.droidevs.counterapp.domain.preference.controle.VibrationOnPreference): SetVibrationOnUseCase =
        SetVibrationOnUseCase(pref)

    @Provides
    @Singleton
    fun provideGetLabelControlUseCase(pref: io.droidevs.counterapp.domain.preference.controle.LabelControlPreference): GetLabelControlUseCase =
        GetLabelControlUseCase(pref)

    @Provides
    @Singleton
    fun provideSetLabelControlUseCase(pref: io.droidevs.counterapp.domain.preference.controle.LabelControlPreference): SetLabelControlUseCase =
        SetLabelControlUseCase(pref)

    // display
    @Provides
    @Singleton
    fun provideGetThemeUseCase(pref: io.droidevs.counterapp.domain.preference.display.ThemePreference): GetThemeUseCase = GetThemeUseCase(pref)

    @Provides
    @Singleton
    fun provideSetThemeUseCase(pref: io.droidevs.counterapp.domain.preference.display.ThemePreference): SetThemeUseCase = SetThemeUseCase(pref)

    @Provides
    @Singleton
    fun provideGetHideControlsUseCase(pref: io.droidevs.counterapp.domain.preference.display.HideControlsPreference): GetHideControlsUseCase = GetHideControlsUseCase(pref)

    @Provides
    @Singleton
    fun provideSetHideControlsUseCase(pref: io.droidevs.counterapp.domain.preference.display.HideControlsPreference): SetHideControlsUseCase = SetHideControlsUseCase(pref)

    @Provides
    @Singleton
    fun provideGetHideLastUpdateUseCase(pref: io.droidevs.counterapp.domain.preference.display.HideLastUpdatePreference): GetHideLastUpdateUseCase = GetHideLastUpdateUseCase(pref)

    @Provides
    @Singleton
    fun provideSetHideLastUpdateUseCase(pref: io.droidevs.counterapp.domain.preference.display.HideLastUpdatePreference): SetHideLastUpdateUseCase = SetHideLastUpdateUseCase(pref)

    @Provides
    @Singleton
    fun provideGetKeepScreenOnUseCase(pref: io.droidevs.counterapp.domain.preference.display.KeepScreenOnPreference): GetKeepScreenOnUseCase = GetKeepScreenOnUseCase(pref)

    @Provides
    @Singleton
    fun provideSetKeepScreenOnUseCase(pref: io.droidevs.counterapp.domain.preference.display.KeepScreenOnPreference): SetKeepScreenOnUseCase = SetKeepScreenOnUseCase(pref)

    // notification
    @Provides
    @Singleton
    fun provideGetCounterLimitNotificationUseCase(pref: io.droidevs.counterapp.domain.preference.notification.CounterLimitNotificationPreference): GetCounterLimitNotificationUseCase = GetCounterLimitNotificationUseCase(pref)

    @Provides
    @Singleton
    fun provideSetCounterLimitNotificationUseCase(pref: io.droidevs.counterapp.domain.preference.notification.CounterLimitNotificationPreference): SetCounterLimitNotificationUseCase = SetCounterLimitNotificationUseCase(pref)

    @Provides
    @Singleton
    fun provideGetDailySummaryNotificationUseCase(pref: io.droidevs.counterapp.domain.preference.notification.DailySummaryNotificationPreference): GetDailySummaryNotificationUseCase = GetDailySummaryNotificationUseCase(pref)

    @Provides
    @Singleton
    fun provideSetDailySummaryNotificationUseCase(pref: io.droidevs.counterapp.domain.preference.notification.DailySummaryNotificationPreference): SetDailySummaryNotificationUseCase = SetDailySummaryNotificationUseCase(pref)

    @Provides
    @Singleton
    fun provideGetNotificationSoundUseCase(pref: io.droidevs.counterapp.domain.preference.notification.NotificationSoundPreference): GetNotificationSoundUseCase = GetNotificationSoundUseCase(pref)

    @Provides
    @Singleton
    fun provideSetNotificationSoundUseCase(pref: io.droidevs.counterapp.domain.preference.notification.NotificationSoundPreference): SetNotificationSoundUseCase = SetNotificationSoundUseCase(pref)

    @Provides
    @Singleton
    fun provideGetNotificationVibrationPatternUseCase(pref: io.droidevs.counterapp.domain.preference.notification.NotificationVibrationPatternPreference): GetNotificationVibrationPatternUseCase = GetNotificationVibrationPatternUseCase(pref)

    @Provides
    @Singleton
    fun provideSetNotificationVibrationPatternUseCase(pref: io.droidevs.counterapp.domain.preference.notification.NotificationVibrationPatternPreference): SetNotificationVibrationPatternUseCase = SetNotificationVibrationPatternUseCase(pref)

    // backup
    @Provides
    @Singleton
    fun provideGetAutoBackupUseCase(pref: io.droidevs.counterapp.domain.preference.buckup.AutoBackupPreference): GetAutoBackupUseCase = GetAutoBackupUseCase(pref)

    @Provides
    @Singleton
    fun provideSetAutoBackupUseCase(pref: io.droidevs.counterapp.domain.preference.buckup.AutoBackupPreference): SetAutoBackupUseCase = SetAutoBackupUseCase(pref)

    @Provides
    @Singleton
    fun provideGetBackupIntervalUseCase(pref: io.droidevs.counterapp.domain.preference.buckup.BackupIntervalPreference): GetBackupIntervalUseCase = GetBackupIntervalUseCase(pref)

    @Provides
    @Singleton
    fun provideSetBackupIntervalUseCase(pref: io.droidevs.counterapp.domain.preference.buckup.BackupIntervalPreference): SetBackupIntervalUseCase = SetBackupIntervalUseCase(pref)

    @Provides
    @Singleton
    fun provideGetBackupLocationUseCase(pref: io.droidevs.counterapp.domain.preference.buckup.BackupLocationPreference): GetBackupLocationUseCase = GetBackupLocationUseCase(pref)

    @Provides
    @Singleton
    fun provideSetBackupLocationUseCase(pref: io.droidevs.counterapp.domain.preference.buckup.BackupLocationPreference): SetBackupLocationUseCase = SetBackupLocationUseCase(pref)

    @Provides
    @Singleton
    fun providePreferenceUseCases(
        getCounterIncrementStep: GetCounterIncrementStepUseCase,
        setCounterIncrementStep: SetCounterIncrementStepUseCase,
        getDefaultCounterValue: GetDefaultCounterValueUseCase,
        setDefaultCounterValue: SetDefaultCounterValueUseCase,
        getMinimumCounterValue: GetMinimumCounterValueUseCase,
        setMinimumCounterValue: SetMinimumCounterValueUseCase,
        getMaximumCounterValue: GetMaximumCounterValueUseCase,
        setMaximumCounterValue: SetMaximumCounterValueUseCase,
        getHardwareButtonControl: GetHardwareButtonControlUseCase,
        setHardwareButtonControl: SetHardwareButtonControlUseCase,
        getSoundsOn: GetSoundsOnUseCase,
        setSoundsOn: SetSoundsOnUseCase,
        getVibrationOn: GetVibrationOnUseCase,
        setVibrationOn: SetVibrationOnUseCase,
        getLabelControl: GetLabelControlUseCase,
        setLabelControl: SetLabelControlUseCase,
        getTheme: GetThemeUseCase,
        setTheme: SetThemeUseCase,
        getHideControls: GetHideControlsUseCase,
        setHideControls: SetHideControlsUseCase,
        getHideLastUpdate: GetHideLastUpdateUseCase,
        setHideLastUpdate: SetHideLastUpdateUseCase,
        getKeepScreenOn: GetKeepScreenOnUseCase,
        setKeepScreenOn: SetKeepScreenOnUseCase,
        getCounterLimitNotification: GetCounterLimitNotificationUseCase,
        setCounterLimitNotification: SetCounterLimitNotificationUseCase,
        getDailySummaryNotification: GetDailySummaryNotificationUseCase,
        setDailySummaryNotification: SetDailySummaryNotificationUseCase,
        getNotificationSound: GetNotificationSoundUseCase,
        setNotificationSound: SetNotificationSoundUseCase,
        getNotificationVibrationPattern: GetNotificationVibrationPatternUseCase,
        setNotificationVibrationPattern: SetNotificationVibrationPatternUseCase,
        getAutoBackup: GetAutoBackupUseCase,
        setAutoBackup: SetAutoBackupUseCase,
        getBackupInterval: GetBackupIntervalUseCase,
        setBackupInterval: SetBackupIntervalUseCase,
        getBackupLocation: GetBackupLocationUseCase,
        setBackupLocation: SetBackupLocationUseCase
    ): PreferenceUseCases = PreferenceUseCases(
        getCounterIncrementStep,
        setCounterIncrementStep,
        getDefaultCounterValue,
        setDefaultCounterValue,
        getMinimumCounterValue,
        setMinimumCounterValue,
        getMaximumCounterValue,
        setMaximumCounterValue,
        getHardwareButtonControl,
        setHardwareButtonControl,
        getSoundsOn,
        setSoundsOn,
        getVibrationOn,
        setVibrationOn,
        getLabelControl,
        setLabelControl,
        getTheme,
        setTheme,
        getHideControls,
        setHideControls,
        getHideLastUpdate,
        setHideLastUpdate,
        getKeepScreenOn,
        setKeepScreenOn,
        getCounterLimitNotification,
        setCounterLimitNotification,
        getDailySummaryNotification,
        setDailySummaryNotification,
        getNotificationSound,
        setNotificationSound,
        getNotificationVibrationPattern,
        setNotificationVibrationPattern,
        getAutoBackup,
        setAutoBackup,
        getBackupInterval,
        setBackupInterval,
        getBackupLocation,
        setBackupLocation
    )
}
