package io.droidevs.counterapp.ui.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.ui.models.CounterSnapshot
import io.droidevs.counterapp.ui.vm.CounterViewViewModel

class CounterViewModelFactory(
    val counter : CounterSnapshot,
    val repository: CounterRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CounterViewViewModel::class.java))
            return CounterViewViewModel(
                initialCounter = counter,
                repository = repository
            ) as T
        else
            throw IllegalArgumentException("Unknown ViewModel class")
    }
}