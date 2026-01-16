package io.droidevs.counterapp.domain.usecases.preference.display

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.display.HideControlsPreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetHideControlsUseCase @Inject constructor(
    private val pref: HideControlsPreference,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(value: Boolean): Result<Unit, PreferenceError> = withContext(dispatchers.io) {
        pref.set(value)
    }
}
