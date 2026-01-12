package io.droidevs.counterapp.domain.usecases.preference

import io.droidevs.counterapp.domain.usecases.preference.counter.GetCounterDecrementStepUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.GetCounterIncrementStepUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.SetCounterIncrementStepUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.GetDefaultCounterValueUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.SetDefaultCounterValueUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.GetMaximumCounterValueUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.SetMaximumCounterValueUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.GetMinimumCounterValueUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.SetCounterDecrementStepUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.SetMinimumCounterValueUseCase

data class CounterPreferenceUseCases(
    val getCounterIncrementStep: GetCounterIncrementStepUseCase,
    val setCounterIncrementStep: SetCounterIncrementStepUseCase,
    val getCounterDecrementStep: GetCounterDecrementStepUseCase,
    val setCounterDecrementStep: SetCounterDecrementStepUseCase,
    val getDefaultCounterValue: GetDefaultCounterValueUseCase,
    val setDefaultCounterValue: SetDefaultCounterValueUseCase,
    val getMinimumCounterValue: GetMinimumCounterValueUseCase,
    val setMinimumCounterValue: SetMinimumCounterValueUseCase,
    val getMaximumCounterValue: GetMaximumCounterValueUseCase,
    val setMaximumCounterValue: SetMaximumCounterValueUseCase,
)
