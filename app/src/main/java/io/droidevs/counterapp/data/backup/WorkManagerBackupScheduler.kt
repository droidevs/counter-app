package io.droidevs.counterapp.data.backup

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import io.droidevs.counterapp.domain.backup.BackupConfig
import io.droidevs.counterapp.domain.backup.BackupScheduleError
import io.droidevs.counterapp.domain.backup.BackupScheduler
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.runCatchingResult
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkManagerBackupScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) : BackupScheduler {

    override suspend fun apply(config: BackupConfig): Result<Unit, BackupScheduleError> =
        runCatchingResult(
            errorTransform = { e -> BackupScheduleError.Unknown(e) }
        ) {
            val request = PeriodicWorkRequestBuilder<io.droidevs.counterapp.internal.AutoBackupWorker>(
                config.intervalHours,
                TimeUnit.HOURS
            ).build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    WORK_NAME,
                    ExistingPeriodicWorkPolicy.UPDATE,
                    request
                )

            Unit
        }

    override suspend fun cancel(): Result<Unit, BackupScheduleError> =
        runCatchingResult(
            errorTransform = { e -> BackupScheduleError.Unknown(e) }
        ) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
            Unit
        }

    private companion object {
        private const val WORK_NAME = "auto_backup"
    }
}
