package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.ui.models.CounterUiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.time.Instant

class HomeViewModel(
    val counterRepository: CounterRepository,
    val categoryRepository: CategoryRepository
) : ViewModel() {

    private var activeCounter: Counter? = null
    private var interactionJob: Job? = null

    val countersSnapshots = counterRepository.getLastEditedWithCategory(6)
        .onStart { emit(emptyList()) }
        .map { counters ->
            counters.map {
                it.toUiModel()
            }
        }

    val countersNumber = counterRepository.getTotalCounters()
        .onStart { emit(0) }

    val categoriesCount = categoryRepository.getTotalCategoriesCount()
        .onStart { emit(0) }


    val categories = categoryRepository.topCategories(3)
        .onStart { emit(emptyList()) }
        .map { categories ->
            categories.map {
                it.toUiModel()
            }
        }

    fun incrementCounter(counter: CounterUiModel) {
        if (activeCounter != null && activeCounter!!.id != counter.id) {
            flushInteraction(counter.toDomain())
        }
        activeCounter = counter.toDomain()
        var c = activeCounter!!
        c.increment()
        viewModelScope.launch {
            counterRepository.saveCounter(c)
        }
    }

    fun decrementCounter(counter: CounterUiModel) {
        if (activeCounter != null && activeCounter!!.id != counter.id) {
            flushInteraction(counter.toDomain())
        }
        activeCounter = counter.toDomain()
        var c = activeCounter!!

        c.decrement()
        viewModelScope.launch {
            counterRepository.saveCounter(c)
        }
        scheduleInteractionEnd(c)
    }

    private fun scheduleInteractionEnd(counter: Counter) {
        interactionJob?.cancel()

        interactionJob = viewModelScope.launch {
            delay(600) // user stopped tapping
            finishInteraction(counter)
        }
    }

    private fun finishInteraction(counter: Counter) {
        counter.apply {
            orderAnchorAt = Instant.now()
        }
        viewModelScope.launch {
            counterRepository.saveCounter(
                counter
            )
        }

        activeCounter = null
    }

    private fun flushInteraction(counter: Counter) {
        interactionJob?.cancel()
        counter.orderAnchorAt = Instant.now()
        viewModelScope.launch {
            counterRepository.saveCounter(
                counter
            )
        }
    }

    override fun onCleared() {
        activeCounter?.let { flushInteraction(it) }
    }


}