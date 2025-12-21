package io.droidevs.counterapp.ui.listeners

import io.droidevs.counterapp.ui.models.CounterUiModel

interface OnCounterClickListener {
    fun onCounterClick(counter: CounterUiModel)
}