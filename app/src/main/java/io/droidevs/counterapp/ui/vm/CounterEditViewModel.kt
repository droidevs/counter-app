package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.droidevs.counterapp.model.Counter
import io.droidevs.counterapp.ui.CounterSnapshot
import io.droidevs.counterapp.ui.toDomain
import io.droidevs.counterapp.ui.toSnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.Instant

class CounterEditViewModel(
    initialCounter: CounterSnapshot
) : ViewModel() {

    // Backing state
    private val _counter = MutableStateFlow(initialCounter.toDomain())
    val counter: StateFlow<CounterSnapshot> = _counter
        .asStateFlow()
        .map {
            it.toSnapshot()
        } as StateFlow<CounterSnapshot>


    // Update name
    fun updateName(name: String) {
        val c = _counter.value
        _counter.value = Counter(
            id = c.id,
            name = name,
            currentCount = c.currentCount,
            canIncrease = c.canIncrease,
            canDecrease = c.canDecrease,
            createdAt = c.createdAt,
            lastUpdatedAt = c.lastUpdatedAt
        )
    }

    // Update current count
    fun updateCurrentCount(count: Int) {
        val c = _counter.value
        _counter.value = Counter(
            id = c.id,
            name = c.name,
            currentCount = count,
            canIncrease = c.canIncrease,
            canDecrease = c.canDecrease,
            createdAt = c.createdAt,
            lastUpdatedAt = c.lastUpdatedAt
        )
    }

    // Update flags
    fun setCanIncrease(canIncrease: Boolean) {
        val c = _counter.value
        _counter.value = Counter(
            id = c.id,
            name = c.name,
            currentCount = c.currentCount,
            canIncrease = canIncrease,
            canDecrease = c.canDecrease,
            createdAt = c.createdAt,
            lastUpdatedAt = c.lastUpdatedAt
        )
    }

    fun setCanDecrease(canDecrease: Boolean) {
        val c = _counter.value
        _counter.value = Counter(
            id = c.id,
            name = c.name,
            currentCount = c.currentCount,
            canIncrease = c.canIncrease,
            canDecrease = canDecrease,
            createdAt = c.createdAt,
            lastUpdatedAt = c.lastUpdatedAt
        )
    }

    // Save logic (to repository or database)
    fun save(onSaved: (() -> Unit)? = null) {
        viewModelScope.launch {
            // TODO: persist _counter.value to your repository
            onSaved?.invoke()
        }
    }
}
