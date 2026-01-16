package io.droidevs.counterapp.domain.usecases.preference.counter

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.counter.MinimumCounterValuePreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetMinimumCounterValueUseCase @Inject constructor(
    private val pref: MinimumCounterValuePreference,
    private val dispatchers: DispatcherProvider
) {
    operator fun invoke(): Flow<Result<Int?, PreferenceError>> = pref.get().flowOn(dispatchers.io)
}
