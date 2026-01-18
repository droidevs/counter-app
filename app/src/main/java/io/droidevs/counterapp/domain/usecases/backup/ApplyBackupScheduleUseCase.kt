package io.droidevs.counterapp.domain.usecases.backup

import io.droidevs.counterapp.domain.backup.BackupScheduler
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.RootError
import io.droidevs.counterapp.domain.result.errors.InternalError
import io.droidevs.counterapp.domain.result.errors.UnknownError
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Reads the latest BackupConfig from preferences and applies it to the scheduler.
 * Call this after any preference change and also on app start.
 */
class ApplyBackupScheduleUseCase @Inject constructor(
    private val getBackupConfigUseCase: GetBackupConfigUseCase,
    private val scheduler: BackupScheduler
) {
    suspend operator fun invoke(): Result<Unit, RootError> {
        return when (val cfg = getBackupConfigUseCase().first()) {
            is Result.Success -> {
                val config = cfg.data
                val scheduled = if (config.enabled) scheduler.apply(config) else scheduler.cancel()
                when (scheduled) {
                    is Result.Success -> Result.Success(Unit)
                    is Result.Failure -> Result.Failure(UnknownError("Failed to apply auto-backup schedule"))
                }
            }
            is Result.Failure -> Result.Failure(InternalError("Failed to read backup preferences"))
        }
    }
}
