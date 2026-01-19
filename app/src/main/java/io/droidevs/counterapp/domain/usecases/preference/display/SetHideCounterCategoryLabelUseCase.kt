package io.droidevs.counterapp.domain.usecases.preference.display

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.controle.LabelControlPreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Display preference: hide the counter category label/chip.
 *
 * Storage is currently backed by [LabelControlPreference] for legacy reasons.
 */
class SetHideCounterCategoryLabelUseCase @Inject constructor(
    private val pref: LabelControlPreference,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(value: Boolean): Result<Unit, PreferenceError> = withContext(dispatchers.io) {
        pref.set(value)
    }
}

