package io.droidevs.counterapp.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.models.CounterWithCategoryUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class CountersListViewModel @Inject constructor(
    private val counterUseCases: CounterUseCases
) : ViewModel() {

    // key = Counter.key, value = Counter domain object
    private val visibleCounters = mutableMapOf<String, Counter>()
    private val pendingReorderCounters = mutableMapOf<String, Counter>()


    val counters: Flow<List<CounterWithCategoryUiModel>> = counterUseCases.getCountersWithCategories()
        .onStart { emit(emptyList()) }
        .map { counters ->
            counters.map {
                it.toUiModel()
            }
        }


    fun increment(counter: CounterUiModel) {
        val c = counter.toDomain()
        c.increment()
        viewModelScope.launch {
            counterUseCases.updateCounter(UpdateCounterRequest.of(counterId = c.id, newCount = c.currentCount))
        }

        pendingReorderCounters[c.id] = c
        Log.i("CountersListViewModel", "increment: ${pendingReorderCounters.size}")
    }

    fun decrement(counter: CounterUiModel) {
        val c = counter.toDomain()
        c.decrement()
        viewModelScope.launch {
            counterUseCases.updateCounter(UpdateCounterRequest.of(counterId = c.id, newCount = c.currentCount))
        }
        pendingReorderCounters[c.id] = c
    }

    fun onVisibleItemsChanged(items: Set<CounterUiModel>) {
        // Items that WERE visible but are NOT anymore
        val currentlyVisibleKeys = items.map { it.id }.toSet()

        val noLongerVisibleKeys = visibleCounters.keys - currentlyVisibleKeys

        noLongerVisibleKeys.forEach { id ->
            pendingReorderCounters[id]?.let { counter ->
                Log.i("CountersListViewModel", "flushReorder: ${counter.currentCount}")
                flushReorder(counter)
            }
        }

        visibleCounters.clear()
        visibleCounters.putAll(items.associateBy { it.id }.mapValues { it.value.toDomain() })
    }

    private fun flushReorder(counter: Counter) {
        pendingReorderCounters.remove(counter.id)

        viewModelScope.launch {
            counterUseCases.updateCounter(
                UpdateCounterRequest.of(
                    counterId = counter.id,
                    newCount = counter.currentCount
                )
            )
        }
    }

    fun flushAllPendingReorders() {
        pendingReorderCounters.forEach { id, counter ->
            flushReorder(counter)
        }
        pendingReorderCounters.clear()
    }

}
