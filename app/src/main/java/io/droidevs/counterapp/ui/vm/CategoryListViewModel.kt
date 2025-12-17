package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.toUiModel
import kotlinx.coroutines.flow.map

class CategoryListViewModel(
    repository: CategoryRepository
) : ViewModel() {

    val categories = repository.allCategories()
            .map { categories ->
                 categories.map { category ->
                     category.toUiModel()
                 }
            }
}