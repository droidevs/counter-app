package io.droidevs.counterapp.di.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.data.preference.impl.backup.AutoBackupPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.backup.BackupIntervalPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.backup.BackupLocationPreferenceImpl
import io.droidevs.counterapp.domain.preference.buckup.AutoBackupPreference
import io.droidevs.counterapp.domain.preference.buckup.BackupIntervalPreference
import io.droidevs.counterapp.domain.preference.buckup.BackupLocationPreference
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BackupPreferenceModule {

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
}
