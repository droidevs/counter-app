package io.droidevs.counterapp.domain.usecases.preference.buckup

import io.droidevs.counterapp.domain.preference.buckup.BackupLocationPreference
import kotlinx.coroutines.flow.Flow

class GetBackupLocationUseCase(private val pref: BackupLocationPreference) {
    operator fun invoke(): Flow<String> = pref.get()
}

