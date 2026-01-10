package io.droidevs.counterapp.domain.preference

import io.droidevs.counterapp.domain.preference.counter.CounterIncrementStepPreference
import io.droidevs.counterapp.domain.preference.counter.DefaultCounterValuePreference
import io.droidevs.counterapp.domain.preference.counter.MaximumCounterValuePreference
import io.droidevs.counterapp.domain.preference.counter.MinimumCounterValuePreference

// 2. Counter Behavior Group
data class CounterBehaviorPreferences(
    val incrementStep: CounterIncrementStepPreference,
    val defaultValue: DefaultCounterValuePreference,
    val minimumValue: MinimumCounterValuePreference,
    val maximumValue: MaximumCounterValuePreference
)