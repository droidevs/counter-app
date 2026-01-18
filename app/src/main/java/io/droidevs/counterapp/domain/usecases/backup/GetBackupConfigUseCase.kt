package io.droidevs.counterapp.domain.usecases.backup

import io.droidevs.counterapp.domain.backup.BackupConfig
import io.droidevs.counterapp.domain.backup.BackupLocation
import io.droidevs.counterapp.domain.preference.buckup.AutoBackupPreference
import io.droidevs.counterapp.domain.preference.buckup.BackupIntervalPreference
import io.droidevs.counterapp.domain.preference.buckup.BackupLocationPreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.dataOr
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetBackupConfigUseCase @Inject constructor(
    private val autoBackupPreference: AutoBackupPreference,
    private val backupIntervalPreference: BackupIntervalPreference,
    private val backupLocationPreference: BackupLocationPreference
) {
    operator fun invoke(): Flow<Result<BackupConfig, PreferenceError>> = combine(
        autoBackupPreference.get(),
        backupIntervalPreference.get(),
        backupLocationPreference.get()
    ) { autoBackup, interval, location ->
        when {
            autoBackup is Result.Failure -> Result.Failure(autoBackup.error)
            interval is Result.Failure -> Result.Failure(interval.error)
            location is Result.Failure -> Result.Failure(location.error)
            else -> {
                val enabled = autoBackup.dataOr { false }
                val hours = interval.dataOr { 24L }.coerceAtLeast(1L)
                val loc = BackupLocation.fromPreferenceValue(location.dataOr { "local" })
                Result.Success(BackupConfig(enabled, hours, loc))
            }
        }
    }
}

