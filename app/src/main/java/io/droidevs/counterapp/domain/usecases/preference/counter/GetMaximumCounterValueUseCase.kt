package io.droidevs.counterapp.domain.usecases.preference.counter

import io.droidevs.counterapp.domain.preference.counter.MaximumCounterValuePreference
import kotlinx.coroutines.flow.Flow

class GetMaximumCounterValueUseCase(private val pref: MaximumCounterValuePreference) {
    operator fun invoke(): Flow<Int?> = pref.get()
}

