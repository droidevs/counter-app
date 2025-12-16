package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import io.droidevs.counterapp.domain.toSnapshot
import io.droidevs.counterapp.model.Counter
import io.droidevs.counterapp.ui.CounterSnapshot
import io.droidevs.counterapp.ui.toDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class CounterViewViewModel(
    initialCounter : CounterSnapshot
) : ViewModel() {

    // modern approch better than live data
    private val _counter = MutableStateFlow<Counter?>(null)

    val counter  = _counter
        .asStateFlow()
        .onStart { emit(initialCounter.toDomain()) }
        .map { counter ->
            counter?.toSnapshot()
        }

    fun increment() {
        val c = _counter.value ?: return
        c.increment()
    }

    fun decrement() {
        val c = _counter.value ?: return
        c.decrement()
    }

    fun reset() {
        val c = _counter.value ?: return
        c.reset()
    }
}