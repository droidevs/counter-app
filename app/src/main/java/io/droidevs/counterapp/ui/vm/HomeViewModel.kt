package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import io.droidevs.counterapp.data.CounterRepository
import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.toSnapshot
import io.droidevs.counterapp.domain.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class HomeViewModel(val counterRepository: CounterRepository) : ViewModel() {


    val countersSnapshots = counterRepository.getAllCounters()
        .onStart { emit(emptyList()) }
        .map { counters ->
            counters.map {
                it.toSnapshot()
            }
        }

    val countersNumber = counterRepository.getTotalCounters()
        .onStart { emit(0) }


    val _categories : MutableStateFlow<List<Category>> = MutableStateFlow(emptyList())

    val categories = _categories
        .asStateFlow()
        .onStart { loadCategories() }
        .map { categories ->
            categories.map {
                it.toUiModel()
            }
        }

    fun loadCategories() {

        val categories = listOf(
            Category("1", "Fitness", 8),
            Category("2", "Habits", 5),
            Category("3", "Work", 3)
        )
        _categories.value = categories
    }
}