package io.droidevs.counterapp.domain.usecases.preference.counter

import io.droidevs.counterapp.domain.preference.counter.DefaultCounterValuePreference
import kotlinx.coroutines.flow.Flow

class GetDefaultCounterValueUseCase(private val pref: DefaultCounterValuePreference) {
    operator fun invoke(): Flow<Int> = pref.get()
}

