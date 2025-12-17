package io.droidevs.counterapp.ui.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.ui.vm.CategoryListViewModel

class CategoryListViewModelFactory(
    val repository: CategoryRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryListViewModel::class.java))
            return CategoryListViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}