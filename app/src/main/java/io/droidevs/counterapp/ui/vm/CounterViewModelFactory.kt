package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.data.CounterRepository
import io.droidevs.counterapp.ui.CounterSnapshot

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