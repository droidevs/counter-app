package io.droidevs.counterapp.domain.system

enum class CounterNature {
    /** Grows over time */
    CUMULATIVE,

    /** Trigger-based */
    EVENT
}
