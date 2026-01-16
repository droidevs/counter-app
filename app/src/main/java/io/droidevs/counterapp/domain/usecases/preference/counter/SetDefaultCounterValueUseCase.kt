package io.droidevs.counterapp.domain.usecases.preference.counter

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.counter.DefaultCounterValuePreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetDefaultCounterValueUseCase @Inject constructor(
    private val pref: DefaultCounterValuePreference,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(value: Int): Result<Unit, PreferenceError> = withContext(dispatchers.io) {
        pref.set(value)
    }
}
