package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.ui.CounterSnapshot

class CounterViewModelFactory(
    val counter : CounterSnapshot
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CounterViewViewModel::class.java))
            return CounterViewViewModel(counter) as T
        else
            throw IllegalArgumentException("Unknown ViewModel class")
    }
}