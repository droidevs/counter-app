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
    fun provideGetCounterIncrementStepUseCase(pref: CounterIncrementStepPreference): GetCounterIncrementStepUseCase =
        GetCounterIncrementStepUseCase(pref)

    @Provides
    @Singleton
    fun provideSetCounterIncrementStepUseCase(pref: CounterIncrementStepPreference): SetCounterIncrementStepUseCase =
        SetCounterIncrementStepUseCase(pref)

    @Provides
    @Singleton
    fun provideGetDefaultCounterValueUseCase(pref: DefaultCounterValuePreference): GetDefaultCounterValueUseCase =
        GetDefaultCounterValueUseCase(pref)

    @Provides
    @Singleton
    fun provideSetDefaultCounterValueUseCase(pref: DefaultCounterValuePreference): SetDefaultCounterValueUseCase =
        SetDefaultCounterValueUseCase(pref)

    @Provides
    @Singleton
    fun provideGetMinimumCounterValueUseCase(pref: MinimumCounterValuePreference): GetMinimumCounterValueUseCase =
        GetMinimumCounterValueUseCase(pref)

    @Provides
    @Singleton
    fun provideSetMinimumCounterValueUseCase(pref: MinimumCounterValuePreference): SetMinimumCounterValueUseCase =
        SetMinimumCounterValueUseCase(pref)

    @Provides
    @Singleton
    fun provideGetMaximumCounterValueUseCase(pref: MaximumCounterValuePreference): GetMaximumCounterValueUseCase =
        GetMaximumCounterValueUseCase(pref)

    @Provides
    @Singleton
    fun provideSetMaximumCounterValueUseCase(pref: MaximumCounterValuePreference): SetMaximumCounterValueUseCase =
        SetMaximumCounterValueUseCase(pref)

    // controle
    @Provides
    @Singleton
    fun provideGetHardwareButtonControlUseCase(pref: HardwareButtonControlPreference): GetHardwareButtonControlUseCase =
        GetHardwareButtonControlUseCase(pref)

    @Provides
    @Singleton
    fun provideSetHardwareButtonControlUseCase(pref: HardwareButtonControlPreference): SetHardwareButtonControlUseCase =
        SetHardwareButtonControlUseCase(pref)

    @Provides
    @Singleton
    fun provideGetSoundsOnUseCase(pref: SoundsOnPreference): GetSoundsOnUseCase =
        GetSoundsOnUseCase(pref)

    @Provides
    @Singleton
    fun provideSetSoundsOnUseCase(pref: SoundsOnPreference): SetSoundsOnUseCase =
        SetSoundsOnUseCase(pref)

    @Provides
    @Singleton
    fun provideGetVibrationOnUseCase(pref: VibrationOnPreference): GetVibrationOnUseCase =
        GetVibrationOnUseCase(pref)

    @Provides
    @Singleton
    fun provideSetVibrationOnUseCase(pref: VibrationOnPreference): SetVibrationOnUseCase =
        SetVibrationOnUseCase(pref)

    @Provides
    @Singleton
    fun provideGetLabelControlUseCase(pref: LabelControlPreference): GetLabelControlUseCase =
        GetLabelControlUseCase(pref)

    @Provides
    @Singleton
    fun provideSetLabelControlUseCase(pref: LabelControlPreference): SetLabelControlUseCase =
        SetLabelControlUseCase(pref)

    // display
    @Provides
    @Singleton
    fun provideGetThemeUseCase(pref: ThemePreference): GetThemeUseCase = GetThemeUseCase(pref)

    @Provides
    @Singleton
    fun provideSetThemeUseCase(pref: ThemePreference): SetThemeUseCase = SetThemeUseCase(pref)

    @Provides
    @Singleton
    fun provideGetHideControlsUseCase(pref: HideControlsPreference): GetHideControlsUseCase = GetHideControlsUseCase(pref)

    @Provides
    @Singleton
    fun provideSetHideControlsUseCase(pref: HideControlsPreference): SetHideControlsUseCase = SetHideControlsUseCase(pref)

    @Provides
    @Singleton
    fun provideGetHideLastUpdateUseCase(pref: HideLastUpdatePreference): GetHideLastUpdateUseCase = GetHideLastUpdateUseCase(pref)

    @Provides
    @Singleton
    fun provideSetHideLastUpdateUseCase(pref: HideLastUpdatePreference): SetHideLastUpdateUseCase = SetHideLastUpdateUseCase(pref)

    @Provides
    @Singleton
    fun provideGetKeepScreenOnUseCase(pref: KeepScreenOnPreference): GetKeepScreenOnUseCase = GetKeepScreenOnUseCase(pref)

    @Provides
    @Singleton
    fun provideSetKeepScreenOnUseCase(pref: KeepScreenOnPreference): SetKeepScreenOnUseCase = SetKeepScreenOnUseCase(pref)

    // notification
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

    // backup
    @Provides
    @Singleton
    fun provideGetAutoBackupUseCase(pref: AutoBackupPreference): GetAutoBackupUseCase = GetAutoBackupUseCase(pref)

    @Provides
    @Singleton
    fun provideSetAutoBackupUseCase(pref: AutoBackupPreference): SetAutoBackupUseCase = SetAutoBackupUseCase(pref)

    @Provides
    @Singleton
    fun provideGetBackupIntervalUseCase(pref: BackupIntervalPreference): GetBackupIntervalUseCase = GetBackupIntervalUseCase(pref)

    @Provides
    @Singleton
    fun provideSetBackupIntervalUseCase(pref: BackupIntervalPreference): SetBackupIntervalUseCase = SetBackupIntervalUseCase(pref)

    @Provides
    @Singleton
    fun provideGetBackupLocationUseCase(pref: BackupLocationPreference): GetBackupLocationUseCase = GetBackupLocationUseCase(pref)

    @Provides
    @Singleton
    fun provideSetBackupLocationUseCase(pref: BackupLocationPreference): SetBackupLocationUseCase = SetBackupLocationUseCase(pref)

    @Provides
    @Singleton
    fun providePreferenceUseCases(
        getCounterUseCases: CounterPreferenceUseCases,
        getHardwarePreferenceUseCases: HardwarePreferenceUseCases,
        getDisplayPreferenceUseCases: DisplayPreferenceUseCases,
        getNotificationPreferenceUseCases: NotificationPreferenceUseCases,
        getBackupPreferenceUseCases: BackupPreferenceUseCases
    ): PreferenceUseCases = PreferenceUseCases(
        counterUseCases = getCounterUseCases,
        hardwareUseCases = getHardwarePreferenceUseCases,
        displayUseCases = getDisplayPreferenceUseCases,
        notificationUseCases = getNotificationPreferenceUseCases,
        backupUseCases = getBackupPreferenceUseCases
    )
}
