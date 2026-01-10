package io.droidevs.counterapp.ui.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.ui.vm.CategoryViewViewModel

class CategoryViewViewModelFactory(
    private val categoryId: String,
    private val categoryUseCases: CategoryUseCases
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoryViewViewModel(
                categoryId = categoryId,
                categoryUseCases = categoryUseCases
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
