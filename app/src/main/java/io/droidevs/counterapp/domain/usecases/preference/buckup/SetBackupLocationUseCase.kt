package io.droidevs.counterapp.domain.usecases.preference.buckup

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.buckup.BackupLocationPreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetBackupLocationUseCase @Inject constructor(
    private val pref: BackupLocationPreference,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(value: String): Result<Unit, PreferenceError> = withContext(dispatchers.io) {
        pref.set(value)
    }
}
