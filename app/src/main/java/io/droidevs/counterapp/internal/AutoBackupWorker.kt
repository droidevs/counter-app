package io.droidevs.counterapp.internal

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.droidevs.counterapp.domain.usecases.backup.RunAutoBackupUseCase

@HiltWorker
class AutoBackupWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val runAutoBackupUseCase: RunAutoBackupUseCase
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val r = runAutoBackupUseCase()
        return when (r) {
            is io.droidevs.counterapp.domain.result.Result.Success -> Result.success()
            is io.droidevs.counterapp.domain.result.Result.Failure -> {
                // For now: auto-backup should be resilient. If it fails, retry.
                // (We can refine based on specific error types later.)
                Result.retry()
            }
        }
    }
}
