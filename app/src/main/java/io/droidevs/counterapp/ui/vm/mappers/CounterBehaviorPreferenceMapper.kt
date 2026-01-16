package io.droidevs.counterapp.ui.vm.mappers

import io.droidevs.counterapp.ui.vm.states.CounterBehaviorPreferenceUiState


@Deprecated("This mapper is no longer used and will be removed in a future version.")
fun Quintuple<Int, Int, Int, Int?, Int?>.toCounterBehaviorPreferenceUiState(): CounterBehaviorPreferenceUiState {
    return CounterBehaviorPreferenceUiState(
        counterIncrementStep = first,
        counterDecrementStep = second,
        defaultCounterValue = third,
        minimumCounterValue = fourth,
        maximumCounterValue = fifth
    )
}
