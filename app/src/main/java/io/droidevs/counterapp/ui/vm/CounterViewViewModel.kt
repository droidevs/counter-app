package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toSnapshot
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.requests.DeleteCounterRequest
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import io.droidevs.counterapp.ui.CounterSnapshotParcelable
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterViewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val counterUseCases: CounterUseCases
) : ViewModel() {

    private val initialCounter: CounterUiModel = savedStateHandle.get<CounterSnapshotParcelable>("counter")
        ?.toUiModel() ?: throw IllegalArgumentException("Counter argument is required")

    // modern approch better than live data
    private val _counter = MutableStateFlow<Counter?>(this.initialCounter.toDomain())

    val counter = _counter
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
            _counter.value?.let {
                counterUseCases.updateCounter(UpdateCounterRequest.of(counterId = it.id, newCount = it.currentCount))
            }
        }
    }

    fun delete() {
        viewModelScope.launch {
            _counter.value?.let {
                counterUseCases.deleteCounter(DeleteCounterRequest.of(counterId = it.id))
            }
        }
    }
}
