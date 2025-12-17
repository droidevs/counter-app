package io.droidevs.counterapp.ui.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.ui.vm.CreateCounterViewModel

class CreateCounterViewModelFactory(
    private val repository : CounterRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateCounterViewModel::class.java)) {
            return CreateCounterViewModel(
                repository = repository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}