package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CreateCounterViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateCounterViewModel::class.java)) {
            return CreateCounterViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}