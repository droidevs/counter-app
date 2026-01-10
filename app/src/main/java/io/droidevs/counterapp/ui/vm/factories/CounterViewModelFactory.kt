package io.droidevs.counterapp.ui.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.vm.CounterViewViewModel

class CounterViewModelFactory(
    val counter : CounterUiModel,
    val counterUseCases: CounterUseCases
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CounterViewViewModel::class.java))
            return CounterViewViewModel(
                initialCounter = counter,
                counterUseCases = counterUseCases
            ) as T
        else
            throw IllegalArgumentException("Unknown ViewModel class")
    }
}