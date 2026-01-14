package io.droidevs.counterapp.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.domain.preference.buckup.AutoBackupPreference
import io.droidevs.counterapp.domain.preference.buckup.BackupIntervalPreference
import io.droidevs.counterapp.domain.preference.buckup.BackupLocationPreference
import io.droidevs.counterapp.domain.usecases.preference.BackupPreferenceUseCases
import io.droidevs.counterapp.domain.usecases.preference.buckup.GetAutoBackupUseCase
import io.droidevs.counterapp.domain.usecases.preference.buckup.GetBackupIntervalUseCase
import io.droidevs.counterapp.domain.usecases.preference.buckup.GetBackupLocationUseCase
import io.droidevs.counterapp.domain.usecases.preference.buckup.SetAutoBackupUseCase
import io.droidevs.counterapp.domain.usecases.preference.buckup.SetBackupIntervalUseCase
import io.droidevs.counterapp.domain.usecases.preference.buckup.SetBackupLocationUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BackupUseCaseModule {

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
    fun provideBackupPreferenceUseCases(
        getAutoBackupUseCase: GetAutoBackupUseCase,
        setAutoBackupUseCase: SetAutoBackupUseCase,
        getBackupIntervalUseCase: GetBackupIntervalUseCase,
        setBackupIntervalUseCase: SetBackupIntervalUseCase,
        getBackupLocationUseCase: GetBackupLocationUseCase,
        setBackupLocationUseCase: SetBackupLocationUseCase
    ): BackupPreferenceUseCases = BackupPreferenceUseCases(
        getAutoBackup = getAutoBackupUseCase,
        setAutoBackup = setAutoBackupUseCase,
        getBackupInterval = getBackupIntervalUseCase,
        setBackupInterval = setBackupIntervalUseCase,
        getBackupLocation = getBackupLocationUseCase,
        setBackupLocation = setBackupLocationUseCase
    )
}