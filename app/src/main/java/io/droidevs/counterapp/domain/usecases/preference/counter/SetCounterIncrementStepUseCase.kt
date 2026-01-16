package io.droidevs.counterapp.domain.usecases.preference.counter

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.counter.CounterIncrementStepPreference
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetCounterIncrementStepUseCase @Inject constructor(
    private val pref: CounterIncrementStepPreference,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(value: Int) = withContext(dispatchers.io) {
        pref.set(value)
    }
}
