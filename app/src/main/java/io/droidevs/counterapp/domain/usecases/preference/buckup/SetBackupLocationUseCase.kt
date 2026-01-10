package io.droidevs.counterapp.domain.usecases.preference.buckup

import io.droidevs.counterapp.domain.preference.buckup.BackupLocationPreference

class SetBackupLocationUseCase(private val pref: BackupLocationPreference) {
    suspend operator fun invoke(value: String) = pref.set(value)
}

