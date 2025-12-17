package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.ui.models.CategoryWithCountersUiModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class CategoryViewViewModel(
    val categoryId: String,
    val repository: CategoryRepository
) : ViewModel() {

    val category = repository.categoryWithCounters(categoryId = categoryId)
        .map { category ->
            category.toUiModel()
        }


}

