package io.droidevs.counterapp.domain.backup

import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.RootError
import io.droidevs.counterapp.domain.result.errors.Error

/** Schedules/cancels auto-backup work. */
interface BackupScheduler {
    suspend fun apply(config: BackupConfig): Result<Unit, BackupScheduleError>
    suspend fun cancel(): Result<Unit, BackupScheduleError>
}

sealed class BackupScheduleError : RootError {
    data class Unknown(val cause: Throwable) : BackupScheduleError()
}
