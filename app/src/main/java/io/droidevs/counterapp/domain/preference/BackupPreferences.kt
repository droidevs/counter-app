package io.droidevs.counterapp.domain.preference

import io.droidevs.counterapp.domain.preference.buckup.AutoBackupPreference
import io.droidevs.counterapp.domain.preference.buckup.BackupIntervalPreference
import io.droidevs.counterapp.domain.preference.buckup.BackupLocationPreference

data class BackupPreferences(
    val autoBackup: AutoBackupPreference,
    val backupInterval: BackupIntervalPreference,
    val backupLocation: BackupLocationPreference
)