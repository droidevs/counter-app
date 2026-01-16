package io.droidevs.counterapp.domain.usecases.preference.counter

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.counter.MaximumCounterValuePreference
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetMaximumCounterValueUseCase @Inject constructor(
    private val pref: MaximumCounterValuePreference,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(value: Int?) = withContext(dispatchers.io) {
        pref.set(value)
    }
}
