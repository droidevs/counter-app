package io.droidevs.counterapp.domain.counter

/** Effective counter behavior for an operation after resolving overrides + optional global defaults. */
data class CounterBehavior(
    val incrementStep: Int,
    val decrementStep: Int,
    val minValue: Int?,
    val maxValue: Int?,
    val defaultValue: Int,
)

