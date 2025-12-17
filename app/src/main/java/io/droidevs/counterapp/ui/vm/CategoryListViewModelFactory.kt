package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.data.CategoryRepository

class CategoryListViewModelFactory(
    val repository: CategoryRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryListViewModel::class.java))
            return CategoryListViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}