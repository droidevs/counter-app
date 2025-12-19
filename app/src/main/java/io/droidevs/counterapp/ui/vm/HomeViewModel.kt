package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.ui.models.CounterSnapshot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(
    val counterRepository: CounterRepository,
    val categoryRepository: CategoryRepository
) : ViewModel() {


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

    fun incrementCounter(counter: CounterSnapshot) {
        var c = counter.toDomain()
        c.increment()
        viewModelScope.launch {
            counterRepository.saveCounter(c)
        }
    }

    fun decrementCounter(counter: CounterSnapshot) {
        var c = counter.toDomain()
        c.decrement()
        viewModelScope.launch {
            counterRepository.saveCounter(c)
        }
    }

}