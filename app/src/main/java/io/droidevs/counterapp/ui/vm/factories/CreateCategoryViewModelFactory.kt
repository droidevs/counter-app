package io.droidevs.counterapp.ui.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.ui.vm.CreateCategoryViewModel

class CreateCategoryViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateCategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateCategoryViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}