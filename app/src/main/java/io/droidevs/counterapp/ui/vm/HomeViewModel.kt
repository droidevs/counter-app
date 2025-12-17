package io.droidevs.counterapp.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.droidevs.counterapp.data.CounterRepository
import io.droidevs.counterapp.domain.model.CounterCategory
import io.droidevs.counterapp.domain.toSnapshot
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.ui.CounterCategoryUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

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


    val _categories : MutableStateFlow<List<CounterCategory>> = MutableStateFlow(emptyList())

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
            CounterCategory("1", "Fitness", 8),
            CounterCategory("2", "Habits", 5),
            CounterCategory("3", "Work", 3)
        )
        _categories.value = categories
    }
}