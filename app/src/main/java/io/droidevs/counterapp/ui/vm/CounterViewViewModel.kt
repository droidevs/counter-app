package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.droidevs.counterapp.domain.toSnapshot
import io.droidevs.counterapp.model.Counter
import io.droidevs.counterapp.ui.CounterSnapshot
import io.droidevs.counterapp.ui.toDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map


class CounterViewViewModel(
    initialCounter : CounterSnapshot
) : ViewModel() {

    // modern approch better than live data
    private val _counter = MutableStateFlow<Counter?>(initialCounter.toDomain())

    val counter  = _counter
        .asStateFlow()
        .map { counter ->
            counter?.toSnapshot()
        }

    fun getCounter() = _counter.value?.toSnapshot()

    fun increment() {
        val c = _counter.value ?: return
        c.increment()
        _counter.value = Counter(
            id = c.id,
            name = c.name,
            currentCount = c.currentCount,
            canIncrease = c.canIncrease,
            canDecrease = c.canDecrease,
            createdAt = c.createdAt,
            lastUpdatedAt = c.lastUpdatedAt
        )
    }

    fun decrement() {
        val c = _counter.value ?: return
        c.decrement()
        _counter.value = Counter(
            id = c.id,
            name = c.name,
            currentCount = c.currentCount,
            canIncrease = c.canIncrease,
            canDecrease = c.canDecrease,
            createdAt = c.createdAt,
            lastUpdatedAt = c.lastUpdatedAt
        )
    }

    fun reset() {
        val c = _counter.value ?: return
        c.reset()
        _counter.value = Counter(
            id = c.id,
            name = c.name,
            currentCount = c.currentCount,
            canIncrease = c.canIncrease,
            canDecrease = c.canDecrease,
            createdAt = c.createdAt,
            lastUpdatedAt = c.lastUpdatedAt
        )
    }
}