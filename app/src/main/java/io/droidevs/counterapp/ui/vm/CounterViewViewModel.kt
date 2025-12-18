package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.droidevs.counterapp.domain.toSnapshot
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.ui.models.CounterSnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class CounterViewViewModel(
    initialCounter : CounterSnapshot,
    val repository: CounterRepository
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
        save()
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
        save()
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
        save()
    }

    fun save() {
        viewModelScope.launch {
            _counter.value?.let { repository.saveCounter(it) }
        }
    }

    fun delete() {
        viewModelScope.launch {
            _counter.value?.let { repository.deleteCounter(it) }
        }
    }
}