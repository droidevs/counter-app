package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.toUiModel
import kotlinx.coroutines.flow.map

class CategoryListViewModel(
    repository: CategoryRepository
) : ViewModel() {

    val categories = lazy {
            repository.allCategories()
            .map { categories ->
                categories.map { category ->
                    category.toUiModel()
                }
            }
    }

    val systemCategories = lazy {
        repository.getSystemCategories()
            .map { categories ->
                categories.map { category ->
                    category.toUiModel()
                }
            }
    }
}