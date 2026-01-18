package io.droidevs.counterapp.domain.usecases.backup

import io.droidevs.counterapp.domain.backup.BackupScheduler
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.RootError
import io.droidevs.counterapp.domain.result.resultSuspendFromFlow
import javax.inject.Inject

/**
 * Reads the latest BackupConfig from preferences and applies it to the scheduler.
 * Call this after any preference change and also on app start.
 */
class ApplyBackupScheduleUseCase @Inject constructor(
    private val getBackupConfigUseCase: GetBackupConfigUseCase,
    private val scheduler: BackupScheduler
) {
    suspend operator fun invoke(): Result<Unit, RootError> =
        resultSuspendFromFlow {
            // read config from flow, then apply schedule
            getBackupConfigUseCase()
                .combineSuspended { config ->
                    if (config.enabled) scheduler.apply(config) else scheduler.cancel()
                }
        }
}
