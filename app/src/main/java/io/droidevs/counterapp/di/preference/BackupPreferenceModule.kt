package io.droidevs.counterapp.di.preference

import android.util.Log
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.BuildConfig
import io.droidevs.counterapp.data.preference.impl.backup.AutoBackupPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.backup.BackupIntervalPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.backup.BackupLocationPreferenceImpl
import io.droidevs.counterapp.domain.preference.buckup.AutoBackupPreference
import io.droidevs.counterapp.domain.preference.buckup.BackupIntervalPreference
import io.droidevs.counterapp.domain.preference.buckup.BackupLocationPreference
import io.droidevs.counterapp.preference.backup.DummyAutoBackupPreference
import io.droidevs.counterapp.preference.backup.DummyBackupIntervalPreference
import io.droidevs.counterapp.preference.backup.DummyBackupLocationPreference
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BackupPreferenceModule {

    @Provides
    @Singleton
    fun provideAutoBackupPreference(dataStore: Lazy<DataStore<Preferences>>): AutoBackupPreference = 
        if (BuildConfig.DEBUG) {
            Log.i("BackupPreferenceModule", "Providing DummyAutoBackupPreference in DEBUG mode")
            DummyAutoBackupPreference()
        } else {
            AutoBackupPreferenceImpl(dataStore.get())
        }

    @Provides
    @Singleton
    fun provideBackupIntervalPreference(dataStore: Lazy<DataStore<Preferences>>): BackupIntervalPreference = 
        if (BuildConfig.DEBUG) {
            DummyBackupIntervalPreference()
        } else {
            BackupIntervalPreferenceImpl(dataStore.get())
        }

    @Provides
    @Singleton
    fun provideBackupLocationPreference(dataStore: Lazy<DataStore<Preferences>>): BackupLocationPreference = 
        if (BuildConfig.DEBUG) {
            DummyBackupLocationPreference()
        } else {
            BackupLocationPreferenceImpl(dataStore.get())
        }
}
