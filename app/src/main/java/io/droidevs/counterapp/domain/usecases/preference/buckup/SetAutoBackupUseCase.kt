package io.droidevs.counterapp.domain.usecases.preference.buckup

import io.droidevs.counterapp.domain.preference.buckup.AutoBackupPreference

class SetAutoBackupUseCase(private val pref: AutoBackupPreference) {
    suspend operator fun invoke(value: Boolean) = pref.set(value)
}

