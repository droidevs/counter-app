package io.droidevs.counterapp.domain.usecases.preference.display

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.display.HideControlsPreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetHideControlsUseCase @Inject constructor(
    private val pref: HideControlsPreference,
    private val dispatchers: DispatcherProvider
) {
    operator fun invoke(): Flow<Result<Boolean, PreferenceError>> = pref.get().flowOn(dispatchers.io)
}
