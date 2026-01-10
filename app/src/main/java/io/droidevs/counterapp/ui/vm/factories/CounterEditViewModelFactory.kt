package io.droidevs.counterapp.ui.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.vm.CounterEditViewModel

class CounterEditViewModelFactory(
    private val initialCounter: CounterUiModel,
    private val counterUseCases: CounterUseCases
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CounterEditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CounterEditViewModel(
                initialCounter = initialCounter,
                counterUseCases = counterUseCases
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
