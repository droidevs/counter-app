package io.droidevs.counterapp.domain.usecases.preference.buckup

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.buckup.AutoBackupPreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAutoBackupUseCase @Inject constructor(
    private val pref: AutoBackupPreference,
    private val dispatchers: DispatcherProvider
) {
    operator fun invoke(): Flow<Result<Boolean, PreferenceError>> = pref.get().flowOn(dispatchers.io)
}
