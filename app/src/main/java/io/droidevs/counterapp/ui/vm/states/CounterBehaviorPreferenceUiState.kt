package io.droidevs.counterapp.ui.vm.states

data class CounterBehaviorPreferenceUiState(
    val isLoading: Boolean = false,
    val counterIncrementStep: Int = 1,
    val counterDecrementStep: Int = 1,
    val defaultCounterValue: Int = 0,
    val maximumCounterValue: Int? = null,
    val minimumCounterValue: Int? = null,
    val error: Boolean = false
)
