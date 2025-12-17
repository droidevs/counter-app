package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import io.droidevs.counterapp.data.CategoryRepository
import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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