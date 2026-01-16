package io.droidevs.counterapp.domain.usecases.preference.counter

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.counter.MaximumCounterValuePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetMaximumCounterValueUseCase @Inject constructor(
    private val pref: MaximumCounterValuePreference,
    private val dispatchers: DispatcherProvider
) {
    operator fun invoke(): Flow<Int?> = pref.get().flowOn(dispatchers.io)
}
