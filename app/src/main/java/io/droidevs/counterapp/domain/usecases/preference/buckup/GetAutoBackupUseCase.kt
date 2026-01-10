package io.droidevs.counterapp.domain.usecases.preference.buckup

import io.droidevs.counterapp.domain.preference.buckup.AutoBackupPreference
import kotlinx.coroutines.flow.Flow

class GetAutoBackupUseCase(private val pref: AutoBackupPreference) {
    operator fun invoke(): Flow<Boolean> = pref.get()
}

