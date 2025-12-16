package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.ui.CounterSnapshot

class CounterEditViewModelFactory(
    private val initialCounter : CounterSnapshot
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CounterEditViewModel::class.java)) {
            return CounterEditViewModel(initialCounter) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}