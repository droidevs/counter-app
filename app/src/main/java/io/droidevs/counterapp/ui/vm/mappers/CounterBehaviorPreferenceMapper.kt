package io.droidevs.counterapp.ui.vm.mappers

import io.droidevs.counterapp.ui.vm.states.CounterBehaviorPreferenceUiState

fun Triple<Int, Int, Pair<Int?, Int?>>.toCounterBehaviorPreferenceUiState(): CounterBehaviorPreferenceUiState {
    val (incrementStep, defaultValue, minMaxPair) = this
    val (minimumValue, maximumValue) = minMaxPair

    return CounterBehaviorPreferenceUiState(
        counterIncrementStep = incrementStep,
        defaultCounterValue = defaultValue,
        minimumCounterValue = minimumValue ?: 0,
        maximumCounterValue = maximumValue ?: 1000
    )
}
