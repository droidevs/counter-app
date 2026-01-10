package io.droidevs.counterapp.domain.usecases.preference.buckup

import io.droidevs.counterapp.domain.preference.buckup.BackupIntervalPreference

class SetBackupIntervalUseCase(private val pref: BackupIntervalPreference) {
    suspend operator fun invoke(value: Long) = pref.set(value)
}

