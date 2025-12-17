package io.droidevs.counterapp.ui.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.data.CategoryRepository
import io.droidevs.counterapp.ui.vm.CreateCategoryViewModel

class CreateCategoryViewModelFactory(
    private val repository: CategoryRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateCategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateCategoryViewModel(
                repository = repository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}