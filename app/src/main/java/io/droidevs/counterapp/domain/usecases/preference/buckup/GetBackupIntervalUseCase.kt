package io.droidevs.counterapp.domain.usecases.preference.buckup

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.buckup.BackupIntervalPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetBackupIntervalUseCase @Inject constructor(
    private val pref: BackupIntervalPreference,
    private val dispatchers: DispatcherProvider
) {
    operator fun invoke(): Flow<Long> = pref.get().flowOn(dispatchers.io)
}
