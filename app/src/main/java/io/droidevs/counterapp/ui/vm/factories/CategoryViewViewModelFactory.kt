package io.droidevs.counterapp.ui.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.ui.vm.CategoryViewViewModel

class CategoryViewViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoryViewViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
