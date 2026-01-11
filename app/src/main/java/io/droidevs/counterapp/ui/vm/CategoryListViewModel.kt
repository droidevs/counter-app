package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    categoryUseCases: CategoryUseCases
) : ViewModel() {

    val categories = lazy {
        categoryUseCases.getAllCategories()
            .map { categories ->
                categories.map { category ->
                    category.toUiModel()
                }
            }
    }

    val systemCategories = lazy {
        categoryUseCases.getSystemCategories()
            .map { categories ->
                categories.map { category ->
                    category.toUiModel()
                }
            }
    }
}
