package io.droidevs.counterapp.ui.vm.states

data class CounterBehaviorPreferenceUiState(
    val counterIncrementStep: Int = 1,
    val defaultCounterValue: Int = 0,
    val maximumCounterValue: Int? = null,
    val minimumCounterValue: Int? = null
)
