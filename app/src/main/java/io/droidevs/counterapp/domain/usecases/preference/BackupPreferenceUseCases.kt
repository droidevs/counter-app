package io.droidevs.counterapp.domain.usecases.preference

import io.droidevs.counterapp.domain.usecases.preference.buckup.GetAutoBackupUseCase
import io.droidevs.counterapp.domain.usecases.preference.buckup.SetAutoBackupUseCase
import io.droidevs.counterapp.domain.usecases.preference.buckup.GetBackupIntervalUseCase
import io.droidevs.counterapp.domain.usecases.preference.buckup.SetBackupIntervalUseCase
import io.droidevs.counterapp.domain.usecases.preference.buckup.GetBackupLocationUseCase
import io.droidevs.counterapp.domain.usecases.preference.buckup.SetBackupLocationUseCase

data class BackupPreferenceUseCases(
    val getAutoBackup: GetAutoBackupUseCase,
    val setAutoBackup: SetAutoBackupUseCase,
    val getBackupInterval: GetBackupIntervalUseCase,
    val setBackupInterval: SetBackupIntervalUseCase,
    val getBackupLocation: GetBackupLocationUseCase,
    val setBackupLocation: SetBackupLocationUseCase
)
