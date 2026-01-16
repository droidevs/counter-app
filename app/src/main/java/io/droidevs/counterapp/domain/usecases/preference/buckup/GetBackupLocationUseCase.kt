package io.droidevs.counterapp.domain.usecases.preference.buckup

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.buckup.BackupLocationPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetBackupLocationUseCase @Inject constructor(
    private val pref: BackupLocationPreference,
    private val dispatchers: DispatcherProvider
) {
    operator fun invoke(): Flow<String> = pref.get().flowOn(dispatchers.io)
}
