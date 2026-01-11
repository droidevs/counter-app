package io.droidevs.counterapp.ui.vm.actions

sealed class CounterBehaviorPreferenceAction {
    data class SetCounterIncrementStep(val step: Int) : CounterBehaviorPreferenceAction()
    data class SetDefaultCounterValue(val value: Int) : CounterBehaviorPreferenceAction()
    data class SetMaximumCounterValue(val value: Int? = null) : CounterBehaviorPreferenceAction()
    data class SetMinimumCounterValue(val value: Int? = null) : CounterBehaviorPreferenceAction()
}
