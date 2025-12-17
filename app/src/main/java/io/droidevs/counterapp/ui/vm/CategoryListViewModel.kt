package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class CategoryListViewModel : ViewModel() {

    // Domain state (internal)
    private val _categories = MutableStateFlow<List<Category>>(emptyList())

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
            Category("1", "Daily Habits", 5),
            Category("2", "Fitness", 3),
            Category("3", "Personal", 2),
            Category("4", "Work", 8)
        )
    }
}