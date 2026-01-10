package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.domain.usecases.category.requests.DeleteCategoryRequest
import io.droidevs.counterapp.domain.usecases.category.requests.GetCategoryWithCountersRequest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CategoryViewViewModel(
    val categoryId: String,
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    val category = categoryUseCases.getCategoryWithCounters(
        GetCategoryWithCountersRequest(categoryId = categoryId)
    ).map { category ->
        category.toUiModel()
    }

    fun deleteCategory() {
        viewModelScope.launch {
            categoryUseCases.deleteCategory(
                DeleteCategoryRequest(categoryId = categoryId)
            )
        }
    }
}
