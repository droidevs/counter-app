package io.droidevs.counterapp.ui.listeners

import io.droidevs.counterapp.ui.models.CounterSnapshot

interface OnCounterClickListener {
    fun onCounterClick(counter: CounterSnapshot)
}