package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import io.droidevs.counterapp.ui.CounterSnapshot

class CreateCounterViewModel() : ViewModel() {



    fun saveCounter(
        counter: CounterSnapshot,
        onCounterSaved: () -> Unit
    ) {

        onCounterSaved()
    }
}