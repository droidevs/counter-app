package io.droidevs.counterapp.domain.usecases.preference.controle

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.controle.VibrationOnPreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetVibrationOnUseCase @Inject constructor(
    private val pref: VibrationOnPreference,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(value: Boolean): Result<Unit, PreferenceError> = withContext(dispatchers.io) {
        pref.set(value)
    }
}
