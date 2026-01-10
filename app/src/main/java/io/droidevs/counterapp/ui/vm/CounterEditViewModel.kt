package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toSnapshot
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import io.droidevs.counterapp.ui.models.CounterUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.Instant

class CounterEditViewModel(
    initialCounter: CounterUiModel,
    private val counterUseCases: CounterUseCases
) : ViewModel() {

    // Backing state
    private val _counter = MutableStateFlow<Counter>(initialCounter.toDomain())
    val counter = _counter
        .asStateFlow()
        .map { counter ->
            counter.toSnapshot()
        }
    val _editedCounter = MutableStateFlow<Counter>(initialCounter.toDomain())
    val editedCounter = _editedCounter
        .asStateFlow()


    // Update name
    fun updateName(name: String) {
        val c = _counter.value
        _editedCounter.value = Counter(
            id = c.id,
            name = name,
            currentCount = c.currentCount,
            canIncrease = c.canIncrease,
            canDecrease = c.canDecrease,
            createdAt = c.createdAt,
            lastUpdatedAt = Instant.now()
        )
    }

    // Update current count
    fun updateCurrentCount(count: Int) {
        val c = _counter.value
        _editedCounter.value = Counter(
            id = c.id,
            name = c.name,
            currentCount = count,
            canIncrease = c.canIncrease,
            canDecrease = c.canDecrease,
            createdAt = c.createdAt,
            lastUpdatedAt = Instant.now()
        )
    }

    // Update flags
    fun setCanIncrease(canIncrease: Boolean) {
        val c = _counter.value
        _editedCounter.value = Counter(
            id = c.id,
            name = c.name,
            currentCount = c.currentCount,
            canIncrease = canIncrease,
            canDecrease = c.canDecrease,
            createdAt = c.createdAt,
            lastUpdatedAt = Instant.now()
        )
    }

    fun setCanDecrease(canDecrease: Boolean) {
        val c = _counter.value
        _editedCounter.value = Counter(
            id = c.id,
            name = c.name,
            currentCount = c.currentCount,
            canIncrease = c.canIncrease,
            canDecrease = canDecrease,
            createdAt = c.createdAt,
            lastUpdatedAt = Instant.now()
        )
    }

    // Save logic (to repository or database)
    fun save(onSaved: (() -> Unit)? = null) {
        viewModelScope.launch {
            // Build an UpdateCounterRequest using the edited counter fields
            val edited = _editedCounter.value
            counterUseCases.updateCounter(
                UpdateCounterRequest.of(
                    edited.id,
                    newName = edited.name,
                    newCategoryId = edited.categoryId,
                    newCount = edited.currentCount
                )
            )
            onSaved?.invoke()
        }
    }
}
