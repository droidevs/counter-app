package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import io.droidevs.counterapp.domain.model.CounterCategory
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.ui.CounterCategoryUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class CategoryListViewModel : ViewModel() {

    // Domain state (internal)
    private val _categories = MutableStateFlow<List<CounterCategory>>(emptyList())

    val categories = _categories.asStateFlow()
            .map { categories ->
                 categories.map { category ->
                     category.toUiModel()
                 }
            }

    init {
        loadDummyData()
    }

    private fun loadDummyData() {
        // Temporary fake data
        _categories.value = listOf(
            CounterCategory("1", "Daily Habits", 5),
            CounterCategory("2", "Fitness", 3),
            CounterCategory("3", "Personal", 2),
            CounterCategory("4", "Work", 8)
        )
    }
}