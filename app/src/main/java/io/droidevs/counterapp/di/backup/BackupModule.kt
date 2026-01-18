package io.droidevs.counterapp.di.backup

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.data.backup.LocalBackupStorage
import io.droidevs.counterapp.data.backup.WorkManagerBackupScheduler
import io.droidevs.counterapp.domain.backup.BackupScheduler
import io.droidevs.counterapp.domain.backup.BackupStorage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BackupModule {

    @Provides
    @Singleton
    fun provideBackupScheduler(impl: WorkManagerBackupScheduler): BackupScheduler = impl

    @Provides
    @Singleton
    fun provideBackupStorage(impl: LocalBackupStorage): BackupStorage = impl
}

