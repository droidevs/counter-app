package io.droidevs.counterapp.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.ui.models.CounterUiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.time.Instant
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest

class HomeViewModel(
    private val counterUseCases: CounterUseCases,
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    private var activeCounter: Counter? = null
    private var interactionJob: Job? = null

    // Usecases-backed flows
    val countersSnapshots = counterUseCases.getLimitCountersWithCategory(6)
        .onStart { emit(emptyList()) }
        .map { counters -> counters.map { it.toUiModel() } }

    val countersNumber = counterUseCases.getTotalNumberOfCounters()
        .onStart { emit(0) }

    val categoriesCount = categoryUseCases.getTotalCategoriesCount()
        .onStart { emit(0) }


    val categories = categoryUseCases.getTopCategories(3)
        .onStart { emit(emptyList()) }
        .map { categories -> categories.map { it.toUiModel() } }

    private fun scheduleInteractionEnd(counter: Counter) {
        interactionJob?.cancel()

        interactionJob = viewModelScope.launch {
            delay(2000) // user stopped tapping
            finishInteraction(counter)
        }
    }

    private fun finishInteraction(counter: Counter) {
        Log.i("HomeViewModel", "finishInteraction: ${counter.currentCount}")
        counter.apply {
            orderAnchorAt = Instant.now()
        }
        viewModelScope.launch {
            counterUseCases.updateCounter(UpdateCounterRequest.of(counterId = counter.id, newCount = counter.currentCount))
        }

        activeCounter = null
    }

    private fun flushInteraction(counter: Counter) {
        interactionJob?.cancel()
        counter.orderAnchorAt = Instant.now()
        viewModelScope.launch {
            counterUseCases.updateCounter(UpdateCounterRequest.of(counterId = counter.id, newCount = counter.currentCount))
        }
    }

    fun incrementCounter(counter: CounterUiModel) {
        if (activeCounter != null && activeCounter!!.id != counter.id) {
            flushInteraction(activeCounter!!)
        }

        activeCounter = counter.toDomain()
        var c = activeCounter!!
        c.increment()
        viewModelScope.launch {
            counterUseCases.updateCounter(UpdateCounterRequest.of(counterId = c.id, newCount = c.currentCount))
        }
        scheduleInteractionEnd(c)
    }

    fun decrementCounter(counter: CounterUiModel) {
        if (activeCounter != null && activeCounter!!.id != counter.id) {
            flushInteraction(activeCounter!!)
        }
        activeCounter = counter.toDomain()
        var c = activeCounter!!

        c.decrement()
        viewModelScope.launch {
            counterUseCases.updateCounter(UpdateCounterRequest.of(counterId = c.id, newCount = c.currentCount))
        }
        scheduleInteractionEnd(c)
    }


    override fun onCleared() {
        activeCounter?.let { flushInteraction(it) }
    }


}
