package io.droidevs.counterapp.domain.usecases.preference.buckup

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.buckup.BackupIntervalPreference
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetBackupIntervalUseCase @Inject constructor(
    private val pref: BackupIntervalPreference,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(value: Long) = withContext(dispatchers.io) {
        pref.set(value)
    }
}
