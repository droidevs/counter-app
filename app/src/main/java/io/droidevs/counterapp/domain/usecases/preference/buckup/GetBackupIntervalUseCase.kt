package io.droidevs.counterapp.domain.usecases.preference.buckup

import io.droidevs.counterapp.domain.preference.buckup.BackupIntervalPreference
import kotlinx.coroutines.flow.Flow

class GetBackupIntervalUseCase(private val pref: BackupIntervalPreference) {
    operator fun invoke(): Flow<Long> = pref.get()
}

