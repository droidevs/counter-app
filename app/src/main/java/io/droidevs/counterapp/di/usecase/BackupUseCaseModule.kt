package io.droidevs.counterapp.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
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
    fun provideGetAutoBackupUseCase(pref: AutoBackupPreference, dispatchers: DispatcherProvider): GetAutoBackupUseCase = GetAutoBackupUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideSetAutoBackupUseCase(pref: AutoBackupPreference, dispatchers: DispatcherProvider): SetAutoBackupUseCase = SetAutoBackupUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideGetBackupIntervalUseCase(pref: BackupIntervalPreference, dispatchers: DispatcherProvider): GetBackupIntervalUseCase = GetBackupIntervalUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideSetBackupIntervalUseCase(pref: BackupIntervalPreference, dispatchers: DispatcherProvider): SetBackupIntervalUseCase = SetBackupIntervalUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideGetBackupLocationUseCase(pref: BackupLocationPreference, dispatchers: DispatcherProvider): GetBackupLocationUseCase = GetBackupLocationUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideSetBackupLocationUseCase(pref: BackupLocationPreference, dispatchers: DispatcherProvider): SetBackupLocationUseCase = SetBackupLocationUseCase(pref, dispatchers)

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
