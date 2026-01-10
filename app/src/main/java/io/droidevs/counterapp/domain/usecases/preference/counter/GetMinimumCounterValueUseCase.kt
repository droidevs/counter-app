package io.droidevs.counterapp.domain.usecases.preference.counter

import io.droidevs.counterapp.domain.preference.counter.MinimumCounterValuePreference
import kotlinx.coroutines.flow.Flow

class GetMinimumCounterValueUseCase(private val pref: MinimumCounterValuePreference) {
    operator fun invoke(): Flow<Int?> = pref.get()
}

