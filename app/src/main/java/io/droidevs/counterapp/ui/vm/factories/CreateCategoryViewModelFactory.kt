package io.droidevs.counterapp.ui.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.ui.vm.CreateCategoryViewModel

@Deprecated("Use Hilt injection with @HiltViewModel instead")
class CreateCategoryViewModelFactory(
    private val categoryUseCases: CategoryUseCases
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateCategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateCategoryViewModel(
                categoryUseCases = categoryUseCases
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}